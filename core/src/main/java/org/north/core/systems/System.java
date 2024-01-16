package org.north.core.systems;

import org.north.core.components.Component;

import java.util.Iterator;
import java.util.List;

// todo: automate "component-to-system" association with annotation processors and reflections
public interface System<E extends Component> {
    Iterator<E> getComponentIterator();
    List<E> getComponentList();
    E getComponent();
    void setCurrentComponent(Component component);
    void addComponent(Component component);
    E getComponent(long componentId);
    E removeComponent(long componentId);
}
