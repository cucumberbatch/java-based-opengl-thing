package ecs;

import ecs.components.Component;
import ecs.components.ComponentType;
import ecs.entities.Entity;
import ecs.systems.System;
import ecs.systems.SystemHandler;
import ecs.util.managment.ComponentFactory;
import ecs.util.managment.Factory;
import ecs.util.managment.SystemFactory;

import java.util.List;

public class Engine implements Runnable {
    private final Thread gameLoopThread;
    private Scene scene;
    private List<Entity> entityList;

    private SystemHandler s_handler = new SystemHandler();
    private Factory<System> s_factory = new SystemFactory();
    private Factory<Component> c_factory = new ComponentFactory();

    public Engine(Scene scene) {
        gameLoopThread = new Thread(this, "GAME_ENGINE_LOOP");
        setScene(scene);
        entityList = scene.entityList;
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
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
        if (!s_handler.hasSystem(type)) {
            s_handler.addSystem(type, s_factory.create(type));
        }
        s_handler.linkComponentAndSystem(type, component);
    }

    public <E extends Component> E instantiateNewComponent(ComponentType type) {
        return (E) c_factory.create(type);
    }

//    public Component getComponentFromSystem(ComponentType type) {
//        return s_handler.getSystemByComponentType(type);
//    }

    public Component removeComponentFromSystem(ComponentType type, Component component) {
        s_handler.removeComponent(type, component);
        return component;
    }
}
