package org.north.core;

import org.north.core.component.InitEntities;
import org.north.core.architecture.entity.Entity;
import org.north.core.config.EngineConfig;
import org.north.core.scene.Scene;

public class MainThread {
    public static void main(String[] args) throws Exception {
        Scene scene = new Scene();
        Entity entity = new Entity("init entity");
        InitEntities initComponent = new InitEntities();
        initComponent.entity = entity;
        entity.addComponent(initComponent);
        scene.addEntity(entity);
        Engine engine = new Engine(EngineConfig.instance);
        engine.setScene(scene);
        engine.run();
    }
}