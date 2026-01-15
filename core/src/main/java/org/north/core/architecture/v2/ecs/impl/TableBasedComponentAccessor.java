package org.north.core.architecture.v2.ecs.impl;

import org.north.core.architecture.v2.ecs.AccessType;
import org.north.core.architecture.v2.ecs.ComponentAccessor;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;

final class TableBasedComponentAccessor<E, C> implements ComponentAccessor<E, C> {
    private final Class<C>   type;
    private final AccessType accessType;

    /**
     * Ссылка на экземпляр компонента, которая будет модифицироваться на
     * каждом вызове метода {@code next()} у итератора по результату запроса сущностей
     *
     * @apiNote не стоит сохранять ссылку на компонент и использовать ее в дальнейшем в конкурентной среде
     */
    private C component;
    private Iterator<C> componentIterator;
    private Lock        columnLock;

    TableBasedComponentAccessor(TableBasedQueryResult<E> queryResult, Class<C> type, AccessType accessType) {
        this.type       = type;
        this.accessType = accessType;

//        queryResult.registerAccessor(this);
    }

    @Override
    public C get(E entity) {
        return component;
    }

    @Override
    public Class<C> getComponentType() {
        return type;
    }

    @Override
    public AccessType getAccessType() {
        return accessType;
    }

    void lockOnTableColumn(Table<E> table) {
        if (this.columnLock != null) {
            this.columnLock.unlock();
        }
        Column<C> column = table.getColumn(type);
        this.columnLock  = getColumnLock(column, accessType);
        this.columnLock.lock();
        this.componentIterator = column.componentIterator();
    }

    void next() {
        component = componentIterator.next();
    }

    void unlock() {
        columnLock.unlock();
    }

    private Lock getColumnLock(Column<C> column, AccessType accessType) {
        return AccessType.READ.equals(accessType)
                ? column.readLock()
                : column.writeLock();
    }

}
