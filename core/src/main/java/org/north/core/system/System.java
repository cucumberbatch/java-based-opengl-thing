package org.north.core.system;

import org.north.core.component.Component;
import org.north.core.management.Resettable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public interface System<E extends Component> extends Resettable {
    Iterator<E> getComponentIterator();
    List<E> getComponentList();
    Collection<E> getComponentUnmodifiableCollection();
    E getComponent(UUID componentId);
    E addComponent(Component component);
    E removeComponent(UUID componentId);
}
