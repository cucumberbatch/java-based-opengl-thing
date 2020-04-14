package ecs.util.managment;

import ecs.systems.System;

public interface Factory<T> {
    T create(System.Type type);
}
