package org.north.core.architecture.v2.ecs.impl;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

final class Table<E> {
    private final Archetype                archetype;
    private final Map<Class<?>, Column<?>> chunkStorage;

    //
    // todo: optimization
    //  Временное решение, в дальнейшем нужно перейти на SparseSet, чтобы
    //  избежать возможности потенциальных коллизий и иметь быстрые lookup'ы
    //
    private final Set<E>  entityIdSet;

    private final List<E> entityIds;
    private final int     chunkCapacity;

    private final ReadWriteLock lock;

    public Table(Archetype archetype, int chunkCapacity, Class<?>[] types) {
        this.archetype    = archetype;
        this.chunkStorage = new IdentityHashMap<>();
        
        this.entityIds    = new ArrayList<>();
        this.entityIdSet  = new HashSet<>();

        this.chunkCapacity = chunkCapacity;

        this.lock = new ReentrantReadWriteLock();

        initChunkStorage(types);
    }

    private void initChunkStorage(Class<?>[] componentTypes) {
        for (Class<?> type : componentTypes) {
            Column<?> column = new Column<>(chunkCapacity, type);
            chunkStorage.put(type, column);
        }
    }

    boolean isSameArchetype(Archetype archetype) {
        return this.archetype.isSame(archetype);
    }

    boolean containsArchetype(Archetype archetype) {
        return this.archetype.contains(archetype);
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

    public Object[] addEntityAndReturnComponents(E entity) {
        Object[] components = null;
        writeLock().lock();
        try {
            boolean didNotContainBefore = entityIdSet.add(entity);
            if (didNotContainBefore) {
                entityIds.add(entity);
                int componentsNumber = chunkStorage.size();
                components = new Object[componentsNumber];

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
    <T> Column<T> getColumn(Class<T> componentType) {
        return (Column<T>) chunkStorage.get(componentType);
    }

    Lock readLock() {
        return lock.readLock();
    }

    Lock writeLock() {
        return lock.writeLock();
    }

    public Iterator<E> entityIterator() {
        return entityIds.iterator();
    }
}
