package ecs;

import ecs.components.Component;
import ecs.entities.Entity;
import ecs.systems.GameLogicUpdater;

public class SceneInitializer {
    private Scene scene;

    public SceneInitializer(Scene scene) {
        this.scene = scene;
    }

    public void initSceneInUpdater(GameLogicUpdater updater) {
        for (Entity entity: scene.entities) {
            for (Component component: entity.components.values()) {
                updater.componentManager.addComponent(entity, component);
            }
        }
    }
}
