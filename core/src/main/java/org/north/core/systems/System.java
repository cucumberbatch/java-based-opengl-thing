package org.north.core.systems;

import org.north.core.components.Component;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

// todo: automate "component-to-system" association with annotation processors and reflections
public interface System<E extends Component> {
    Iterator<E> getComponentIterator();
    List<E> getComponentList();
    E getComponent();
    E getComponent(UUID componentId);
    <C extends Component> C getComponent(Class<C> componentClass);
    void setCurrentComponent(Component component);
    void addComponent(Component component);
    E removeComponent(UUID componentId);
    E removeComponent(Class<E> componentClass);

}
