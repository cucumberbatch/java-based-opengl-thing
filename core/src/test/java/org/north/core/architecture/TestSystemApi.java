package org.north.core.architecture;

import org.junit.jupiter.api.Test;
import org.north.core.architecture.v2.component.AccessType;
import org.north.core.architecture.v2.component.Table;
import org.north.core.architecture.v2.component.ComponentRegistry;
import org.north.core.architecture.v2.entity.Entity;
import org.north.core.architecture.v2.entity.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class TestSystemApi {
    private final Logger logger = LoggerFactory.getLogger(TestSystemApi.class);

    public static class Position {
        float x, y;
    }

    public static class Velocity {
        float dx, dy;
    }

    @Test
    public void testEntityManagerCreation() {
        ComponentRegistry componentRegistry = new ComponentRegistry.Builder()
                .add(Position.class)
                .add(Velocity.class)
                .build();

        EntityManager entityManager =
                new EntityManager(componentRegistry);
    }

    @Test
    public void testEntitySingleCreation() {

    }

    @Test
    public void testEntityQueryInSingleThread() {
        ComponentRegistry componentRegistry = new ComponentRegistry.Builder()
                .add(Position.class)
                .add(Velocity.class)
                .build();

        EntityManager entityManager =
                new EntityManager(componentRegistry);

        int entityCount = 128;

        for (int i = 0; i < entityCount; i++) {
            entityManager.createEntity(Position.class, Velocity.class);
        }

        // Создание объекта запроса сущностей по указанным типам компонентов
        Table.QueryResult<Entity> queryResult =
                entityManager.queryAllWith(Position.class, Velocity.class);

        // Использование try-with-resources конструкции для высвобождения блокировок
        // после выполнения работы с компонентами
        try (queryResult) {

            // Декларирование доступов по типам компонентов (чтение/модифицирование)
            Table.ComponentAccessor<Entity, Velocity> velocityAccessor =
                    new Table.ComponentAccessor<>(queryResult, Velocity.class, AccessType.READ);
            Table.ComponentAccessor<Entity, Position> positionAccessor =
                    new Table.ComponentAccessor<>(queryResult, Position.class, AccessType.WRITE);

            // Итерирование по полученному результату (набору сущностей из итерируемой коллекции архетипов/таблиц)
            for (Entity entity : queryResult) {

                // Возможно обращение к entity
                int entityId = entity.getId();

                float offset = (float) entityId / entityCount;

                // Получение и модификация данных в компонентах происходит через аксессоры ComponentAccess,
                // под капотом аксессора используется итератор из результата запроса сущностей
                Position p = positionAccessor.get();
                Velocity v = velocityAccessor.get();

                p.x = p.x + v.dx + offset;
                p.y = p.y + v.dy - offset;

                logger.info("iterating over components. position -> x: {}\ty: {}", p.x, p.y);
            }
        }
    }

    //@Test
    public void testEntityQueryInMultipleThreads() {
        ComponentRegistry componentRegistry = new ComponentRegistry.Builder()
                .add(Position.class)
                .add(Velocity.class)
                .build();

        EntityManager entityManager =
                new EntityManager(componentRegistry);

        for (int i = 0; i < 64; i++) {
            entityManager.createEntity(Position.class, Velocity.class);
        }

        /* Возможный вид API для добавления сущностей и их компонентов

        Archetype.EntityQueryResult queryResult =
                entityManager.insertAllWith(1_024, Position.class, Velocity.class);

        try (queryResult) {
            Archetype.ComponentAccessor<Velocity> velocityAccessor =
                    new Archetype.ComponentAccessor<>(queryResult, Velocity.class, AccessType.WRITE);
            Archetype.ComponentAccessor<Position> positionAccessor =
                    new Archetype.ComponentAccessor<>(queryResult, Position.class, AccessType.WRITE);

            for (Entity e : queryResult) {

            }
        }
        */


        Future<?> future1 = CompletableFuture.runAsync(() -> {
            // Создание объекта запроса сущностей по указанным типам компонентов
            Table.QueryResult<Entity> queryResult =
                    entityManager.queryAllWith(Position.class, Velocity.class);

            // Использование try-with-resources конструкции для высвобождения блокировок
            // после выполнения работы с компонентами
            try (queryResult) {

                // Декларирование доступов по типам компонентов (чтение/модифицирование)
                Table.ComponentAccessor<Entity, Velocity> velocityAccessor =
                        new Table.ComponentAccessor<>(queryResult, Velocity.class, AccessType.READ);
                Table.ComponentAccessor<Entity, Position> positionAccessor =
                        new Table.ComponentAccessor<>(queryResult, Position.class, AccessType.WRITE);

                // Итерирование по полученному результату (набору сущностей из итерируемой коллекции архетипов/таблиц)
                for (Entity entity : queryResult) {

                    // Получение и модификация данных в компонентах происходит через аксессоры ComponentAccess,
                    // под капотом аксессора используется итератор из результата запроса сущностей
                    Position p = positionAccessor.get();
                    Velocity v = velocityAccessor.get();

                    p.x = (float) (+0.1 * entity.getId() + p.x + v.dx);
                    p.y = (float) (-0.1 * entity.getId() + p.y + v.dy);

                    // Обращение к entity опционально
                    int entityId = entity.getId();

                    logger.info("x: {}\ty: {}", p.x, p.y);
                }
            }
        });

        Future<?> future2 = CompletableFuture.runAsync(() -> {
            entityManager.forAllWith(Position.class, Velocity.class, (p, v) -> {
                p.x = p.x + v.dx;
                p.y = p.y + v.dy;

                logger.info("x: {}\ty: {}", p.x, p.y);
            });
        });

        try {
            future1.get();
            future2.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
