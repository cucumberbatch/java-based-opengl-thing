package org.north.core.system;

import org.north.core.component.Component;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

// todo: automate "component-to-system" association with annotation processors and reflections
public interface System<E extends Component> {
    Iterator<E> getComponentIterator();
    List<E> getComponentList();
    E getComponent(UUID componentId);
    void addComponent(Component component);
    E removeComponent(UUID componentId);
    E removeComponent(Class<E> componentClass);

}
