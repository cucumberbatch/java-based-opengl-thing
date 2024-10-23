package org.north.core.management.memory;

public interface Pool<T> {
    void put(T o);
    T get();
    T create();
}
