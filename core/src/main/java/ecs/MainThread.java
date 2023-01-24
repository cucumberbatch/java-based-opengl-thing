package ecs;

import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.systems.MeshCollider;
import ecs.utils.Logger;
import ecs.utils.Logger.ConsoleLogWriter;
import vectors.Vector3f;
import ecs.systems.ECSSystem;

public class MainThread {
    public static void main(String[] args) {
        Logger.setLogWriter(new ConsoleLogWriter());

        int width = 920;
        int height = 570;

        Engine engine = new Engine("test_engine", width, height, true);

        Scene scene = new Scene();

        Entity initializationEntity = new Entity("initialization entity", engine, scene);
        initializationEntity.addComponent(ECSSystem.Type.INIT_ENTITIES);
        engine.addEntity(initializationEntity);

        engine.run();
    }

}