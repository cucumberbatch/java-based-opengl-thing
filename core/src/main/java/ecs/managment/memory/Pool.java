package ecs.managment.memory;

import ecs.managment.factory.IFactory;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> implements IPool<T> {

    protected IFactory<T> IFactory;
    protected List<T> list;

    public Pool(int capacity, IFactory<T> IFactory) {
        this.IFactory = IFactory;
        list = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            list.add(IFactory.create());
        }
    }

    public Pool(IFactory<T> IFactory) {
        this(100, IFactory);
    }

    @Override
    public T create() {
        return IFactory.create();
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
