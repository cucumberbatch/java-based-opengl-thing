package ecs.util.managment.memory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPool<T> implements Pool<T> {

    protected List<T> list = new ArrayList<>();

    @Override
    public void put(T o) {
        list.add(o);
    }

    @Override
    public T get() {
        return list.isEmpty() ? create() : list.remove(0);
    }
}
