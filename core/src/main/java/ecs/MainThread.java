package ecs;

import ecs.components.InitEntities;
import ecs.config.EngineConfig;
import ecs.entities.Entity;
import ecs.graphics.Window;
import ecs.utils.Logger;

import java.io.InputStream;

public class MainThread {
    public static void main(String[] args) {
//        Logger.printOnStartInfo();


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