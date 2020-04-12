package ecs;

import ecs.components.Component;
import ecs.components.ComponentType;
import ecs.entities.Entity;

import java.util.List;

public class Engine implements Runnable {
    private final Thread gameLoopThread;
    public Scene scene;
    public List<Entity> entityList;

    public SystemHandler    s_handler = new SystemHandler();

    public Engine(Scene scene) {
        gameLoopThread = new Thread(this, "GAME_ENGINE_LOOP");
        setScene(scene);
        entityList = scene.entityList;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    // ---------------------  Game engine processes  -----------------------------------------------------------

    public void tick(float deltaTime) {
        s_handler.update(deltaTime);
    }

    public void start() {

    }

    public void update(double deltaTime) {

    }

    public void render() {

    }

    // ---------------------  Running method for main game engine thread  ---------------------------------------

    @Override
    public void run() {

    }

    // ---------------------  Component access implementation  --------------------------------------------------

    public void addComponentToSystem(ComponentType type, Component component) {
        s_handler.linkComponentAndSystem(type, component);
    }

    public Component instantiateNewComponent(ComponentType type) {
        return type.getComponent().getInstance();
    }

//    public Component getComponentFromSystem(ComponentType type) {
//        return s_handler.getSystemByComponentType(type);
//    }

    public Component removeComponentFromSystem(ComponentType type, Component component) {
        s_handler.removeComponent(type, component);
        return component;
    }
}
