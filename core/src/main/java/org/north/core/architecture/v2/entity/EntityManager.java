package org.north.core.architecture.v2.entity;

import org.north.core.architecture.v2.component.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class EntityManager {
    private final ComponentRegistry componentRegistry;
    private final List<Table<Entity>> tables;
    private final AtomicInteger entityIdSequence;

    public EntityManager(ComponentRegistry componentRegistry) {
        this.componentRegistry = componentRegistry;
        this.tables = new ArrayList<>();
        this.entityIdSequence = new AtomicInteger();
    }

//    todo: implement this
    public final Component[] createEntity(Class<?>... componentTypes) {
        Archetype archetype = componentRegistry.createArchetype(componentTypes);

        // Try to find a table with such archetype
        Table<Entity> foundTable = null;
        for (Table<Entity> table : tables) {
            if (table.isSameArchetype(archetype)) {
                foundTable = table;
                break;
            }
        }

        // If we haven't found table then we create a new one
        if (foundTable == null) {
            foundTable = new Table<>(archetype, componentTypes);
            tables.add(foundTable);
        }

        Entity entity = new Entity(entityIdSequence.incrementAndGet());
        foundTable.add(entity);

//        foundArchetype.addEntity(entity);

        //
        // нужен как минимум id или ссылка на entity, чтобы можно было
        // устанавливать начальные значения в данных компонентов новой сущности
        //

        return null;
    }

    private Component[] retrieveComponents(Entity entity, Table<Entity> table, Class<?> componentTypes) {
        return null;
    }

    public final Table.QueryResult<Entity> queryAllWith(Class<?>... componentTypes) {
        return new Table.QueryResult<>(collectArchetypes(componentTypes));
    }

    public final <T1, T2> void forAllWith(Class<T1> firstComponent,
                                          Class<T2> secondComponent,
                                          BiConsumer<T1, T2> processor) {

        try (Table.QueryResult<Entity> queryResult = queryAllWith(firstComponent, secondComponent)) {
            var t1Accessor = new Table.ComponentAccessor<>(queryResult, firstComponent,  AccessType.WRITE);
            var t2Accessor = new Table.ComponentAccessor<>(queryResult, secondComponent, AccessType.WRITE);
            for (Entity entity : queryResult) {
                processor.accept(t1Accessor.get(), t2Accessor.get());
            }
        }
    }

    private Collection<Table<Entity>> collectArchetypes(Class<?>[] types) {
        Collection<Table<Entity>> collectedTables = new ArrayList<>();
        Archetype archetype = componentRegistry.createArchetype(types);
        for (Table<Entity> table : tables) {
            if (table.isContainsArchetype(archetype)) {
                collectedTables.add(table);
            }
        }
        return collectedTables;
    }

}
