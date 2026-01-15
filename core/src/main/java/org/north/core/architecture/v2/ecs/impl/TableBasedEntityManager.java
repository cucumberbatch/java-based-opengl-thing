package org.north.core.architecture.v2.ecs.impl;

import org.north.core.architecture.v2.ecs.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

public class TableBasedEntityManager<E> implements EntityManager<E> {
    private static final int DEFAULT_CHUNK_CAPACITY = 256;

    private final List<Table<E>>    tables;
    private final ComponentRegistry componentRegistry;
    private final AtomicLong        entityIdSequence;
    private final LongFunction<E>   wrapper;
    private final ToLongFunction<E> unwrapper;
    private final int               chunkCapacity;

    public TableBasedEntityManager(Set<Class<?>>     componentTypeSet,
                                   LongFunction<E>   wrapper,
                                   ToLongFunction<E> unwrapper) {
        this(DEFAULT_CHUNK_CAPACITY, componentTypeSet, wrapper, unwrapper);
    }

    public TableBasedEntityManager(int               chunkCapacity,
                                   Set<Class<?>>     componentTypeSet,
                                   LongFunction<E>   wrapper,
                                   ToLongFunction<E> unwrapper) {
        this.componentRegistry = new ComponentRegistry(componentTypeSet);
        this.tables            = new ArrayList<>();
        this.entityIdSequence  = new AtomicLong();
        this.wrapper           = wrapper;
        this.unwrapper         = unwrapper;
        this.chunkCapacity     = chunkCapacity;
    }

    @Override
    public E create() {
        return wrapper.apply(entityIdSequence.incrementAndGet());
    }

    //    todo: implement this
    @Override
    public final Object[] createEntity(Class<?>... componentTypes) {
        Archetype archetype = componentRegistry.createArchetype(componentTypes);

        // Try to find a table with such archetype
        Table<E> foundTable = null;
        for (Table<E> table : tables) {
            if (table.isSameArchetype(archetype)) {
                foundTable = table;
                break;
            }
        }

        // If we haven't found table then we create a new one
        if (foundTable == null) {
            foundTable = new Table<>(archetype, chunkCapacity, componentTypes);
            tables.add(foundTable);
        }

        E entity = wrapper.apply(entityIdSequence.incrementAndGet());
        foundTable.add(entity);

//        foundArchetype.addEntity(entity);

        //
        // нужен как минимум id или ссылка на entity, чтобы можно было
        // устанавливать начальные значения в данных компонентов новой сущности
        //

        return null;
    }

    @Override
    public <C> C add(E entity, C component) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public <C> C add(E entity, Class<C> componentType) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public <C> C get(E entity, Class<C> componentType) {
        throw new UnsupportedOperationException("Not implemented yet...");

//        return tables.stream()
//                .filter(table -> table.contains(entity))
//                .filter(table -> table.getColumn(componentType).)
//                .findFirst()
//                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public <C> boolean has(E entity, Class<C> componentType) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public <C> boolean remove(E entity, Class<C> componentType) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public final QueryResult<E> queryAllWith(Class<?>... componentTypes) {
        return new TableBasedQueryResult<>(collectArchetypes(componentTypes));
    }

    @Override
    public <C> void forAllWith(Class<C> componentType, Consumer<C> processor) {
        final Collection<Table<E>> tables =
                collectArchetypes(new Class<?>[]{componentType});

        try (final QueryResult<E> queryResult = new TableBasedQueryResult<>(tables)) {
            final ComponentAccessor<E, C> accessor =
                    queryResult.accessor(componentType, AccessType.WRITE);

            for (E entity : queryResult) {
                processor.accept(accessor.get(entity));
            }
        }
    }

    @Override
    public final <C1, C2> void forAllWith(Class<C1> firstComponentType,
                                          Class<C2> secondComponentType,
                                          BiConsumer<C1, C2> processor) {

        final Collection<Table<E>> tables =
                collectArchetypes(new Class<?>[]{firstComponentType, secondComponentType});

        try (final QueryResult<E> queryResult = new TableBasedQueryResult<>(tables)) {
            final ComponentAccessor<E, C1> firstAccessor =
                    queryResult.accessor(firstComponentType, AccessType.WRITE);

            final ComponentAccessor<E, C2> secondAccessor =
                    queryResult.accessor(secondComponentType, AccessType.WRITE);

            for (E entity : queryResult) {
                processor.accept(firstAccessor.get(entity), secondAccessor.get(entity));
            }
        }
    }

    private Collection<Table<E>> collectArchetypes(Class<?>[] types) {
        Collection<Table<E>> collectedTables = new ArrayList<>();
        Archetype archetype = componentRegistry.createArchetype(types);
        for (Table<E> table : tables) {
            if (table.containsArchetype(archetype)) {
                collectedTables.add(table);
            }
        }
        return collectedTables;
    }

}
