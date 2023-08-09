package ecs.managment.data.serializer;

import ecs.Scene;

public interface SceneSerializer<T> {
    T serialize(Scene scene);
}
