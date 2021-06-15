package ecs.components;

import ecs.entities.Entity;
import ecs.systems.ECSSystem;

import java.util.UUID;

public abstract class AbstractECSComponent implements ECSComponent {

    /* Name of component */
    public String name;

    /* Entity which this component is belong to */
    public Entity entity;

    /* If you need to execute a special method of the component
    you can get a reference to the system it belongs */
    public ECSSystem<? extends ECSComponent> system;

    /* Reference to the transform component of its entity */
    public Transform transform;

    /* Activity state of component */
    public boolean isActive = true;
    public UUID id;


    @Override
    public void reset() {
        entity = null;
        transform = null;
    }

    /*
     Getters and setters implementation by an abstract component class
     */
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public ECSSystem<? extends ECSComponent> getSystem() {
        return system;
    }

    @Override
    public void setSystem(ECSSystem<? extends ECSComponent> system) {
        this.system = system;
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
    }


    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActivity(boolean activity) {
        this.isActive = activity;
    }

    @Override
    public void switchActivity() {
        isActive = !isActive;
    }

    // TODO: 07.06.2021 implementation isn't done yet
    @Override
    public <E extends ECSComponent> E getInstance() {
        throw new UnsupportedOperationException();
    }

    // TODO: 07.06.2021 implementation isn't done yet
    @Override
    public <E extends ECSComponent> E getReplica() {
        throw new UnsupportedOperationException();
    }

    @Override
    public abstract String toString();
}
