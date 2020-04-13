package ecs.util.managment;

import ecs.components.ComponentType;

public interface Factory<T> {
    T create(ComponentType type);
}
