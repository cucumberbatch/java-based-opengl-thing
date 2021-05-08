package ecs;

import ecs.entities.Entity;
import ecs.systems.System.Type;

public class MainThread {
    public static void main(String[] args) {
        int width = 640;
        int height = 480;

        Engine engine = new Engine("test_engine", width, height, true, new GameLogic());
        Entity e = new Entity(engine, new Scene());
        e.addComponent(Type.RENDERER);
        engine.addEntity(e);
        engine.run();
    }
}