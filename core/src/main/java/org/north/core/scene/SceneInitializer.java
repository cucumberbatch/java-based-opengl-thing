package org.north.core.scene;

import org.north.core.architecture.entity.ComponentManager;
import org.north.core.components.Transform;
import org.north.core.systems.GameLogicUpdater;

import java.io.*;

public class SceneInitializer {
    private final Scene scene;

    public SceneInitializer(Scene scene) {
        this.scene = scene;
    }

    public void initSceneInUpdater(ComponentManager cm) {
        scene.entities.forEach(entity -> entity.components.values()
                .forEach(component -> cm.add(entity, component.getClass())));
    }

    public Transform readSceneFromFile(String filePath) {
        Transform transform;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            transform = (Transform) inputStream.readObject();
            // Logger.info("Read object: " + transform.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return transform;
    }
}
