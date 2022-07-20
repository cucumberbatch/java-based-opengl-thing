package ecs.managment.factory;

import ecs.systems.ECSSystem;

public interface ComponentSystemFactory<T> {
    T create(ECSSystem.Type type);
}
