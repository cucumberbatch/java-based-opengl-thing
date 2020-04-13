package ecs.util.managment.memory;

public interface Pool<T> {

    T create();

    void put(T o);

    T get();

}
