package org.north.core.architecture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.north.core.architecture.v2.ecs.*;
import org.north.core.architecture.v2.ecs.impl.TableBasedEntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestSystemApi {
    private final Logger logger = LoggerFactory.getLogger(TestSystemApi.class);
    
    private static final class Entity {
        private final long id;

        public Entity(long id) { this.id = id; }
        public long getId()    { return id; }
    }

    public static final class Position { float  x,  y; }
    public static final class Velocity { float dx, dy; }
    public static final class Health   { int value;    }
    
    private static final Set<Class<?>> componentTypes = Stream.of(
            Position.class,
            Velocity.class
    ).collect(Collectors.toSet());


    @Test
    public void testEntityManagerCreation() {
        Assertions.assertDoesNotThrow(() ->
                new TableBasedEntityManager<>(componentTypes, Entity::new, Entity::getId)
        );
    }

    @Test
    public void testLongEntityManagerCreation() {
        Assertions.assertDoesNotThrow(() -> 
                new TableBasedEntityManager<>(componentTypes, Long::valueOf, Long::longValue)
        );
    }

    @Test
    public void testEntitySingleCreation() {

    }

    @Test
    public void testEntityQueryInSingleThread() {
        EntityManager<Entity> entityManager =
                new TableBasedEntityManager<>(componentTypes, Entity::new, Entity::getId);

        int entityCount = 128;

        for (int i = 0; i < entityCount; i++) {
            entityManager.createEntity(Position.class, Velocity.class);
        }

        // Создание объекта запроса сущностей по указанным типам компонентов
        QueryResult<Entity> queryResult =
                entityManager.queryAllWith(Position.class, Velocity.class);

        // Использование try-with-resources конструкции для высвобождения блокировок
        // после выполнения работы с компонентами
        try (queryResult) {

            // Декларирование доступов по типам компонентов (чтение/модифицирование)
            ComponentAccessor<Entity, Velocity> velocities =
                    queryResult.accessor(Velocity.class, AccessType.READ);
            ComponentAccessor<Entity, Position> positions =
                    queryResult.accessor(Position.class, AccessType.WRITE);

            // Итерирование по полученному результату (набору сущностей из итерируемой коллекции архетипов/таблиц)
            for (Entity entity : queryResult) {

                // Возможно обращение к entity
                long entityId = entity.id;

                float offset = (float) entityId / entityCount;

                // Устаревшее API:
                //   Получение и модификация данных в компонентах происходит через аксессоры ComponentAccess,
                //   под капотом аксессора используется итератор из результата запроса сущностей
                //
                //       Position p = positions.get();
                //       Velocity v = velocities.get();
                //

                // Новое API:
                //   Теперь мы явно указываем сущность при работе с компонентами, что дает возможность
                //   не использовать итератор по сущностям там, где для этого нет необходимости.
                //   Однако, 
                Position p = positions.get(entity);
                Velocity v = velocities.get(entity);

                p.x = p.x + v.dx + offset;
                p.y = p.y + v.dy - offset;

                logger.info("iterating over components. position -> x: {}\ty: {}", p.x, p.y);
            }
        }
    }

    @Test
    public void testEntityQueryInMultipleThreads() {
        EntityManager<Entity> entityManager =
                new TableBasedEntityManager<>(componentTypes, Entity::new, Entity::getId);

        for (int i = 0; i < 64; i++) {
            entityManager.createEntity(Position.class, Velocity.class);
        }

        /* Возможный вид API для добавления сущностей и их компонентов

        Archetype.EntityQueryResult queryResult =
                entityManager.insertAllWith(1_024, Position.class, Velocity.class);

        try (queryResult) {
            Archetype.ComponentAccessor<Velocity> velocities =
                    new Archetype.ComponentAccessor<>(queryResult, Velocity.class, AccessType.WRITE);
            Archetype.ComponentAccessor<Position> positions =
                    new Archetype.ComponentAccessor<>(queryResult, Position.class, AccessType.WRITE);

            for (Entity e : queryResult) {

            }
        }
        */


        Future<?> future1 = CompletableFuture.runAsync(() -> {
            // Создание объекта запроса сущностей по указанным типам компонентов
            QueryResult<Entity> queryResult =
                    entityManager.queryAllWith(Position.class, Velocity.class);

            // Использование try-with-resources конструкции для высвобождения блокировок
            // после выполнения работы с компонентами
            try (queryResult) {

                // Декларирование доступов по типам компонентов (чтение/модифицирование)
                ComponentAccessor<Entity, Velocity> velocities =
                        queryResult.accessor(Velocity.class, AccessType.READ);
                ComponentAccessor<Entity, Position> positions =
                        queryResult.accessor(Position.class, AccessType.WRITE);

                // Итерирование по полученному результату (набору сущностей из итерируемой коллекции архетипов/таблиц)
                for (Entity entity : queryResult) {

                    // Получение и модификация данных в компонентах происходит через аксессоры ComponentAccess,
                    // под капотом аксессора используется итератор из результата запроса сущностей
                    Position p = positions.get(entity);
                    Velocity v = velocities.get(entity);

                    p.x = (float) (+0.1 * entity.getId() + p.x + v.dx);
                    p.y = (float) (-0.1 * entity.getId() + p.y + v.dy);

                    // Обращение к entity опционально
                    long entityId = entity.id;

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

    @Test
    public void testEntityQueryInMultipleThreads2() {
        EntityManager<Entity> entityManager =
                new TableBasedEntityManager<>(componentTypes, Entity::new, Entity::getId);

        for (int i = 0; i < 128; i++) {
            entityManager.createEntity(Position.class, Velocity.class);
        }

        Future<?> future1 = CompletableFuture.runAsync(() -> {
            QueryResult<Entity> queryResult =
                    entityManager.queryAllWith(Position.class, Velocity.class);
            try (queryResult) {
                ComponentAccessor<Entity, Velocity> velocities =
                        queryResult.accessor(Velocity.class, AccessType.READ);
                ComponentAccessor<Entity, Position> positions =
                        queryResult.accessor(Position.class, AccessType.READ);
                for (Entity entity : queryResult) {
                    Position p = positions.get(entity);
                    Velocity v = velocities.get(entity);

                    p.x = (float) (+0.1 * entity.getId() + p.x + v.dx);
                    p.y = (float) (-0.1 * entity.getId() + p.y + v.dy);

                    logger.info("x: {}\ty: {}", p.x, p.y);
                }
            }
        });

        Future<?> future2 = CompletableFuture.runAsync(() -> {
            QueryResult<Entity> queryResult =
                    entityManager.queryAllWith(Position.class, Velocity.class);
            try (queryResult) {
                ComponentAccessor<Entity, Velocity> velocities =
                        queryResult.accessor(Velocity.class, AccessType.READ);
                ComponentAccessor<Entity, Position> positions =
                        queryResult.accessor(Position.class, AccessType.READ);
                for (Entity entity : queryResult) {
                    Position p = positions.get(entity);
                    Velocity v = velocities.get(entity);

                    p.x = (float) (+0.1 * entity.getId() + p.x + v.dx);
                    p.y = (float) (-0.1 * entity.getId() + p.y + v.dy);

                    logger.info("x: {}\ty: {}", p.x, p.y);
                }
            }
        });

        try {
            future1.get();
            future2.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
