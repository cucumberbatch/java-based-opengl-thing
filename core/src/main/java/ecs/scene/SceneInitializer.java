package ecs.scene;

import ecs.systems.GameLogicUpdater;

public class SceneInitializer {
    private Scene scene;

    public SceneInitializer(Scene scene) {
        this.scene = scene;
    }

    public void initSceneInUpdater(GameLogicUpdater updater) {
        scene.entities.forEach(entity -> entity.components.values()
                .forEach(component -> updater.componentManager.addComponent(entity, component)));
    }
}
