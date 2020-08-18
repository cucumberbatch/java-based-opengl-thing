package ecs.managment.memory;

import ecs.managment.factory.Factory;

import java.util.ArrayList;
import java.util.List;

public class ConcretePool<T> implements Pool<T> {

    protected Factory<T> factory;
    protected List<T> list;

    public ConcretePool(int capacity, Factory<T> factory) {
        this.factory = factory;
        list = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            list.add(factory.create());
        }
    }

    public ConcretePool(Factory<T> factory) {
        this(100, factory);
    }

    @Override
    public T create() {
        return factory.create();
    }

    @Override
    public void put(T o) {
        list.add(o);
    }

    @Override
    public T get() {
        return list.isEmpty() ? create() : list.remove(0);
    }
}
