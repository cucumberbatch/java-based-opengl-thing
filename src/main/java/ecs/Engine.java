package ecs;

import ecs.components.Component;
import ecs.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class Engine implements Runnable {
    private final Thread gameLoopThread;
    public Scene scene;
    public List<Entity> entityList = new ArrayList<>();
    public List<System> systemList = new ArrayList<>();

    public Engine(Scene scene) {
        gameLoopThread = new Thread(this, "GAME_ENGINE_LOOP");
    }

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

    public <E extends Component> void addComponentToSystem(E component) {
//        systemList.add();

    }

    public <E extends Component> E removeComponentFromSystem(E component) {
        return null;
    }

    @Override
    public void run() {

    }
}
