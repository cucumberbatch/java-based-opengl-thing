package org.north.core.architecture.v2.component;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Table<E> {
    private final Archetype archetype;
    private final Map<Class<?>, Column<?>> chunkStorage;

    //
    // todo: optimization
    //  Временное решение, в дальнейшем нужно перейти на SparseSet, чтобы
    //  избежать возможности потенциальных коллизий и иметь быстрые lookup'ы
    //
    private final Set<E> entityIdSet;

    private final List<E> entityIds;

    private final ReadWriteLock lock;

    public Table(Archetype archetype, Class<?>[] types) {
        this.archetype = archetype;
        this.chunkStorage = new IdentityHashMap<>();

        this.entityIds = new ArrayList<>();
        this.entityIdSet = new HashSet<>();

        this.lock = new ReentrantReadWriteLock();

        initChunkStorage(types);
    }

    private void initChunkStorage(Class<?>[] componentTypes) {
        for (Class<?> type : componentTypes) {
            Column<?> column = new Column<>(type);
            chunkStorage.put(type, column);
        }
    }

    public boolean isSameArchetype(Archetype archetype) {
        return this.archetype.isSame(archetype);
    }

    public boolean isContainsArchetype(Archetype archetype) {
        return this.archetype.isContains(archetype);
    }

    /**
     * A query result object which is {@link Iterable} by entity collection
     * and {@link AutoCloseable} with purpose of thread-safe iteration
     * processing over the entities and perform read/write operations
     * via registered set of {@link ComponentAccessor}.
     *
     * @param <E> entity type
     */
    public static class QueryResult<E> implements Iterable<E>, AutoCloseable {
        private final Collection<Table<E>> tables;

        private ComponentAccessor<E, ?>[] accessors;
        private Lock tableLock;

        public QueryResult(Collection<Table<E>> tables) {
            this.tables = tables;
        }

        void registerAccessor(ComponentAccessor<E, ?> accessor) {
            if (accessors == null) {
                accessors = new ComponentAccessor[1];
                accessors[0] = accessor;
            } else {
                int length = accessors.length;
                for (ComponentAccessor<E, ?> registeredAccessor : accessors) {
                    if (accessor.equals(registeredAccessor))
                        throw new IllegalArgumentException(String.format("Component accessor of type %s was already added in query result", accessor.type));
                }
                accessors = Arrays.copyOf(accessors, length + 1);
                accessors[length] = accessor;
            }
        }


        @Override
        public void close() {
            for (ComponentAccessor<E, ?> accessor : accessors) {
                accessor.unlock();
            }
            tableLock.unlock();
        }

        @Override
        public Iterator<E> iterator() {
            return new QueryResultIterator();
        }
        
        private class QueryResultIterator implements Iterator<E> {
            private final Iterator<Table<E>> tableIterator;

            private Iterator<E> entityIterator;
            private E entity;

            QueryResultIterator() {
                this.tableIterator = tables.iterator();
                Table<E> table = tableIterator.next();
                tableLock = table.readLock();
                tableLock.lock();
                this.entityIterator = table.entityIterator();
                this.entity = entityIterator.next();

                lockAccessors(table);
                notifyAccessors();
            }

            @Override
            public boolean hasNext() {
                return entity != null;
            }

            @Override
            public E next() {
                E selected = entity;

                if (!entityIterator.hasNext()) {
                    if (!tableIterator.hasNext()) {
                        entity = null;
                        return selected;
                    } else {
                        Table<E> table = tableIterator.next();
                        tableLock.unlock();
                        tableLock = table.readLock();
                        tableLock.lock();
                        entityIterator = table.entityIterator();

                        lockAccessors(table);
                    }
                }
                entity = entityIterator.next();

                notifyAccessors();

                return selected;
            }

            private void lockAccessors(Table<E> table) {
                Arrays.stream(accessors).forEach(accessor -> accessor.lockOnTableColumn(table));
            }

            private void notifyAccessors() {
                for (ComponentAccessor<E, ?> accessor : accessors) {
                    accessor.next();
                }
            }
        }
    }

    public static class ComponentAccessor<E, T> {
        private final Class<T>   type;
        private final AccessType accessType;

        /**
         * Ссылка на экземпляр компонента, которая будет модифицироваться на
         * каждом вызове метода {@code next()} у итератора по результату запроса сущностей
         * @apiNote не стоит сохранять ссылку на компонент и использовать ее в дальнейшем в конкурентной среде
         */
        private T component;

        private Iterator<T> componentIterator;

        private Lock columnLock;

        public ComponentAccessor(QueryResult<E> queryResult, Class<T> type, AccessType accessType) {
            this.type       = type;
            this.accessType = accessType;
            
            queryResult.registerAccessor(this);
        }

        public T get() {
            return component;
        }

        void lockOnTableColumn(Table<E> table) {
            if (this.columnLock != null) {
                this.columnLock.unlock();
            }
            Column<T> column = table.getColumn(type);
            this.columnLock = getColumnLock(column, accessType);
            this.columnLock.lock();
            this.componentIterator = column.componentIterator();
        }

        void next() {
            component = componentIterator.next();
        }

        private Lock getColumnLock(Column<T> column, AccessType accessType) {
            return AccessType.READ.equals(accessType)
                    ? column.readLock()
                    : column.writeLock();
        }

        void unlock() {
            columnLock.unlock();
        }
    }

    public boolean contains(E entity) {
        readLock().lock();
        try {
            return entityIdSet.contains(entity);
        } finally {
            readLock().unlock();
        }
    }

    /**
     * Inserts an entity
     *
     * @param entity an entity that needs to add
     * @return {@code true} if an element was not containing already in this archetype
     */
    public boolean add(E entity) {
        writeLock().lock();
        try {
            boolean didNotContainBefore = entityIdSet.add(entity);
            if (didNotContainBefore) {
                entityIds.add(entity);
            }
            return didNotContainBefore;
        } finally {
            writeLock().unlock();
        }
    }

    public Component[] addEntityAndReturnComponents(E entity) {
        Component[] components = null;
        writeLock().lock();
        try {
            boolean didNotContainBefore = entityIdSet.add(entity);
            if (didNotContainBefore) {
                entityIds.add(entity);
                int componentsNumber = chunkStorage.size();
                components = new Component[componentsNumber];

            }
            return components;
        } finally {
            writeLock().unlock();
        }
    }

    /**
     * Removes an entity
     *
     * @param entity an entity that needs to remove
     * @return {@code true} if an element was removed as a result of this call
     */
    public boolean remove(E entity) {
        writeLock().lock();
        try {
            boolean removed = entityIdSet.remove(entity);
            if (removed) {
                entityIds.remove(entity);
            }
            return removed;
        } finally {
            writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Column<T> getColumn(Class<T> componentType) {
        return (Column<T>) chunkStorage.get(componentType);
    }

    private Lock readLock() {
        return lock.readLock();
    }

    private Lock writeLock() {
        return lock.writeLock();
    }

    public Iterator<E> entityIterator() {
        return entityIds.iterator();
    }
}
