package ecs.systems;

import ecs.components.Component;
import ecs.entities.IComponentManager;
import ecs.systems.processes.ISystem;

import java.util.List;
import java.util.Set;

public interface System extends ISystem, IComponentManager {

    /* -------------- Getters -------------- */
    List<Component> componentList();

    Set<Component> componentSet();

    Component getComponent(Component component);

    <E extends Component> E currentComponent();


    /* -------------- Setters -------------- */
    void componentList(List<Component> componentList);

    void componentSet(Set<Component> componentSet);

    void addComponent(Component component);

    void currentComponent(Component component);


    /* -------------- Other methods -------------- */
    void removeComponent(Component component);


}
