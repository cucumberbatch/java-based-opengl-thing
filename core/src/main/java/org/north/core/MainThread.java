package org.north.core;

import org.north.core.components.InitEntities;
import org.north.core.architecture.entity.Entity;
import org.north.core.scene.Scene;

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