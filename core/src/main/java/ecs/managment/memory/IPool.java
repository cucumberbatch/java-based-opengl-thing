package ecs.managment.memory;

public interface IPool<T> {

    T create();

    void put(T o);

    T get();

}
