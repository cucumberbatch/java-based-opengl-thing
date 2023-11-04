package ecs;

import ecs.components.InitEntities;
import ecs.entities.Entity;
import ecs.scene.Scene;

public class MainThread {
    public static void main(String[] args) {
        Scene scene = new Scene();
        Entity entity = new Entity("init entity");
        InitEntities initComponent = new InitEntities();
        initComponent.entity = entity;
        entity.addComponent(initComponent);
        scene.addEntity(entity);
        Engine engine = new Engine();
        engine.setScene(scene);
        engine.run();
    }
}