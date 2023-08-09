package ecs.managment.data.serializer;

import ecs.Scene;

public class XMLSceneSerializer implements SceneSerializer<String> {
    @Override
    public String serialize(Scene scene) {
        return "<serialized_scene_data>";
    }
}
