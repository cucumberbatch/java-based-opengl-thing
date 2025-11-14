package org.north.core.architecture.v2.component;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Column<T> {

    //
    // todo: optimization
    //  Класс Column стоит рассмотреть как композицию чанков компонентов (односвязный список).
    //  В теории это может дать больше возможностей параллелизации обработки данных
    //  и улучшенную локальность. Помимо этого доступ к каждому элементу должен осуществляться
    //  через SparseSet (вероятно, он будет находится в Table<E>)
    //

    private static final int DEFAULT_CAPACITY = 1024;

    private final T[] components;
    private final int capacity;
    private final ReadWriteLock lock;

    private int size;


    public Column(Class<T> componentType) {
        this(DEFAULT_CAPACITY, componentType);
    }

    public Column(int capacity, Class<T> componentType) {
        this.size = 0;
        this.capacity = capacity;
        this.components = instantiateComponents(capacity, componentType);
        this.lock = new ReentrantReadWriteLock();
    }

    @SuppressWarnings("unchecked")
    private T[] instantiateComponents(int capacity, Class<T> componentType) {
        try {
            T[] components = (T[]) Array.newInstance(componentType, capacity);
            Constructor<T> constructor = componentType.getDeclaredConstructor();
            for (int i = 0; i < capacity; i++) {
                components[i] = constructor.newInstance();
            }
            return components;
        } catch (Exception e) {
            throw new RuntimeException("Failed to pre-instantiate components", e);
        }
    }

    public Lock readLock() {
        return lock.readLock();
    }

    public Lock writeLock() {
        return lock.writeLock();
    }

    public boolean isFull() {
        return size >= capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public void reset() {
        size = 0;
    }

    public Iterable<T> components() {
        return this::componentIterator;
    }

    public Iterator<T> componentIterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                return components[index++];
            }
        };
    }
}
