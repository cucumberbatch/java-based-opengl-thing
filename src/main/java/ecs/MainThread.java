package ecs;

import ecs.entities.Entity;
import ecs.systems.System.Type;

public class MainThread {
    public static void main(String[] args) {
        Engine engine = new Engine("da gaem!lol", 640, 480, true, new GameLogic());
        Entity e = new Entity(engine, new Scene());
        e.addComponent(Type.RENDERER);
        engine.addEntity(e);
        engine.run();
    }
}