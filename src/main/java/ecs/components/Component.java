package ecs.components;

import ecs.entities.Entity;
import ecs.util.ITurntable;

public class Component implements ITurntable {
    public String componentName;
    public Entity entity;

    /**
     * Reference to the transform component of its entity
     */
    public Transform transform;

    public boolean activity;


    @Override
    public boolean isActive() { return activity; }

    @Override
    public void setActivity(boolean activity) { this.activity = activity; }

    @Override
    public void switchActivity() { activity = !activity; }

}
 