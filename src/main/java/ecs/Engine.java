package ecs;

import ecs.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Engine {
    public List<Entity> entityList = new ArrayList<>();
    public List<System> systemList = new ArrayList<>();


    public void frameRender(double deltaTime) {
        update(deltaTime);
        render();
        entityList.get(0).getClass();
    }

    public void start() {
        componentList.forEach((component -> component.start()));
    }

    public void update(double deltaTime) {
        componentList.forEach((component -> component.update(deltaTime)));
    }

    public void render() {
        componentList.forEach((component -> component.render()));
    }

}
