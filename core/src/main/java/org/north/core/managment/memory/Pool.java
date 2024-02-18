package org.north.core.managment.memory;

public interface Pool<T> {
    void put(T o);
    T get();
    T create();
}
