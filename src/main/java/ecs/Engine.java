package ecs;

import ecs.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class Engine {
    public List<Entity> entityList = new ArrayList<>();
    public List<System> systemList = new ArrayList<>();


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

}
