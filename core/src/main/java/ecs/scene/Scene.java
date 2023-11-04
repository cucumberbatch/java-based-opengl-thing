package ecs.scene;

import ecs.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    public String name;
    public List<Entity> entities = new ArrayList<>();

    public Scene() {
    }

    public Scene(String name) {
        this.name = name;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

}
