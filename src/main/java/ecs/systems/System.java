package ecs.systems;

import ecs.components.Component;
import ecs.entities.IComponentManager;
import ecs.systems.processes.ISystem;

import java.util.List;
import java.util.Set;

public interface System<E extends Component> extends ISystem, IComponentManager {

    /* Type of system */
    enum Type {
        RENDERER,
        TRANSFORM,
        RIGIDBODY;
    }


    /* -------------- Getters -------------- */
    List<E> componentList();

    Set<E> componentSet();

    E currentComponent();

    int getWorkflowMask();

    /* -------------- Setters -------------- */
    void componentList(List<E> componentList);

    void componentSet(Set<E> componentSet);

    void currentComponent(E component);


    /* -------------- Other methods -------------- */
    void addComponent(E component);

    E getComponent(E component);

    void removeComponent(E component);
}
