package org.north.core.system;

import org.north.core.component.Component;
import org.north.core.management.Resettable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public interface System<C extends Component> extends Resettable {
    Iterator<C> getComponentIterator();
    List<C> getComponentList();
    Collection<C> getComponentUnmodifiableCollection();
    C getComponent(UUID componentId);
    C addComponent(Component component);
    C removeComponent(UUID componentId);
}
