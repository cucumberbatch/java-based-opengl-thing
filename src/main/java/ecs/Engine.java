package ecs;

import ecs.components.Component;
import ecs.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class Engine implements Runnable {
    private final Thread gameLoopThread;
    public Scene scene;
    public List<Entity> entityList;
    public List<System> systemList = new ArrayList<>();

    public SystemHelper helper = new SystemHelper();

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

    public void frameRender(double deltaTime) {
        update(deltaTime);
        render();
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

    // ---------------------  Component access interface  -------------------------------------------------------

    public <E extends Component> void addComponentToSystem(E component) {
        String name = component.getClass().getSimpleName();
//        systemList.add();
    }

    public <E extends Component> E removeComponentFromSystem(E component) {
        return null;
    }
}
