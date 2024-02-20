package org.north.core.scene;

import org.north.core.architecture.entity.ComponentManager;
import org.north.core.architecture.entity.Entity;
import org.north.core.component.Transform;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class SceneInitializer {
    private final Scene scene;
    private final Map<UUID, Object> sceneObjects;

    public SceneInitializer(Scene scene) {
        this.scene = scene;
        this.sceneObjects = new HashMap<>();
    }

    public void initSceneInUpdater(ComponentManager cm) {
        scene.entities.forEach(entity -> entity.components.values()
                .forEach(component -> cm.add(entity, component.getClass())));
    }

    public Transform readSceneFromFile(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
//            int entitiesCount = in.readInt();
//            for (int i = 0; i < entitiesCount; i++) {
//                Entity entity = new Entity();
//                entity.deserializeObject(in);
//            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
            Logger.getLogger(SceneInitializer.class.getName()).info("Failed to load scene");
        }
        return null;
    }
}
