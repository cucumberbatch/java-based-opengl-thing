package org.north.core.architecture.v2.ecs.impl;

import org.north.core.architecture.v2.ecs.AccessType;
import org.north.core.architecture.v2.ecs.ComponentAccessor;
import org.north.core.architecture.v2.ecs.QueryResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;

/**
 * A query result object which is {@link Iterable} by entity collection
 * and {@link AutoCloseable} with purpose of thread-safe iteration
 * processing over the entities and perform read/write operations
 * via registered set of {@link TableBasedComponentAccessor}.
 *
 * @param <E> entity type
 */
final class TableBasedQueryResult<E> implements QueryResult<E> {
    private final Collection<Table<E>> tables;

    private TableBasedComponentAccessor<E, ?>[] accessors;
    private Lock tableLock;

    TableBasedQueryResult(Collection<Table<E>> tables) {
        this.tables = tables;
    }

    @Override
    public <C> ComponentAccessor<E, C> accessor(Class<C> componentType) {
        return this.accessor(componentType, AccessType.READ);
    }

    @Override
    public <C> ComponentAccessor<E, C> accessor(Class<C> componentType, AccessType accessType) {
        TableBasedComponentAccessor<E, C> accessor = new TableBasedComponentAccessor<>(this, componentType, accessType);
        if (accessors == null) {
            accessors = new TableBasedComponentAccessor[1];
            accessors[0] = accessor;
        } else {
            int length = accessors.length;
            for (TableBasedComponentAccessor<E, ?> registeredAccessor : accessors) {
                if (accessor.equals(registeredAccessor))
                    throw new IllegalArgumentException(String.format("Component accessor of type %s was already added in query result", accessor.getComponentType()));
            }
            accessors = Arrays.copyOf(accessors, length + 1);
            accessors[length] = accessor;
        }
        return accessor;
    }

    @Override
    public void close() {
        for (TableBasedComponentAccessor<E, ?> accessor : accessors) {
            accessor.unlock();
        }
        tableLock.unlock();
    }

    @Override
    public Iterator<E> iterator() {
        return new QueryResultIterator(tables);
    }

    private class QueryResultIterator implements Iterator<E> {
        private final Iterator<Table<E>> tableIterator;

        private Iterator<E> entityIterator;
        private E entity;

        QueryResultIterator(Collection<Table<E>> tables) {
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
            for (TableBasedComponentAccessor<E, ?> accessor : accessors) {
                accessor.next();
            }
        }
    }
}
