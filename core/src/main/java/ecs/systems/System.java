package ecs.systems;

import ecs.components.*;
import ecs.systems.processes.ISystem;

import java.util.Iterator;
import java.util.List;

// todo: automate "component-to-system" association with annotation processors and reflections
public interface System<E extends Component> extends ISystem {
    Iterator<E> getComponentIterator();
    List<E> getComponentList();
    E getComponent();
    int getWorkflowMask();
    void setCurrentComponent(Component component);
    void addComponent(Component component);
    E getComponent(long componentId);
    E removeComponent(long componentId);
}
