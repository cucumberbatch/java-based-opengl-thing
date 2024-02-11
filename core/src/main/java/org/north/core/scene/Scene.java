package org.north.core.scene;

import org.north.core.architecture.entity.Entity;

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
