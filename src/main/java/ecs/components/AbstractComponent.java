package ecs.components;

import ecs.entities.Entity;
import ecs.systems.System;

public abstract class AbstractComponent implements Component {

    /* Name of component */
    private String name;

    /* Entity which this component is belong to */
    private Entity entity;

    /*
     If you need to execute a special method of the component
     you can get a reference to the system it belongs
     */
    private System system;

    /* Reference to the transform component of its entity */
    private Transform transform;

    /* Activity state of component */
    private boolean activity = true;


    /*
     Getters and setters implementation by an abstract component class
     */
    @Override
    public String name() {
        return name;
    }

    @Override
    public void name(String name) {
        this.name = name;
    }

    @Override
    public Entity entity() {
        return entity;
    }

    @Override
    public void entity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public System system() {
        return system;
    }

    @Override
    public void system(System system) {
        this.system = system;
    }

    @Override
    public Transform transform() {
        return transform;
    }

    @Override
    public void transform(Transform transform) {
        this.transform = transform;
    }


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

    @Override
    public <E extends Component> E getInstance() {
        return null;
    }

    @Override
    public <E extends Component> E getReplica() {
        return null;
    }
}
