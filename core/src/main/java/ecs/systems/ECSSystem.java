package ecs.systems;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.entities.IComponentManager;
import ecs.systems.processes.ISystem;

import java.util.List;
import java.util.Set;

public interface ECSSystem<E extends ECSComponent> extends ISystem, IComponentManager {

    /* Type of system */
    enum Type {
        RENDERER,
        TRANSFORM,
        RIGIDBODY,
        CAMERA,
        PLANE;
    }


    /* -------------- Getters -------------- */
    List<E> getComponentList();

    Set<E> componentSet();

    E getComponent();

    int getWorkflowMask();

    /* -------------- Setters -------------- */
    void componentList(List<E> componentList);

    void componentSet(Set<E> componentSet);

    void setComponent(E component);


    /* -------------- Other methods -------------- */
    void addComponent(E component);

    E getComponent(E component);

    void removeComponent(E component);

    Entity getEntityByName(String name);
}
