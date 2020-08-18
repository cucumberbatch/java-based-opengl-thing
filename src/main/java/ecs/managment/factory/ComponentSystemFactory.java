package ecs.managment.factory;

import ecs.systems.System;

public interface ComponentSystemFactory<T> {
    T create(System.Type type);
}
