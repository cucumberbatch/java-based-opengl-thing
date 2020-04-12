package ecs.components;

import ecs.entities.Entity;
import ecs.systems.System;
import ecs.util.Instantiatable;
import ecs.util.Turntable;

public class Component implements Turntable, Instantiatable<Component> {
    public String name;
    public Entity entity;

    /*
     If you need to execute a special method of the component
     you can get a reference to the system it belows
    */
    public System system;

    /* Reference to the transform component of its entity */
    public Transform transform;

    public boolean activity;


    @Override
    public boolean isActive() {
        return activity;
    }

    @Override
    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    @Override
    public void switchActivity() {
        activity = !activity;
    }

    /**
     * Because of its a base component, it will return null
     * instead of component instance
     *
     * @return null in any case
     */
    @Override
    public Component getInstance() {
        return null;
    }
}
 