package org.north.core.architecture.entity;

import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.exception.ComponentAlreadyExistsException;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public interface ComponentContainer {
    Map<Class<? extends Component>, Component> getComponentMap();

    Transform getTransform();

    void setTransform(Transform transform);

    default boolean has(Class<? extends Component> type) {
        return getComponentMap().containsKey(type);
    }

    @SuppressWarnings("unchecked")
    default <ComponentInstance extends Component> ComponentInstance get(Class<ComponentInstance> type) {
        return (ComponentInstance) getComponentMap().get(type);
    }

    default void add(Component component) {
        Class<? extends Component> componentType = component.getClass();
        Map<Class<? extends Component>, Component> componentMap = getComponentMap();

        if (componentMap.containsKey(componentType))
            throw new ComponentAlreadyExistsException(componentType);

        componentMap.put(componentType, component);

        // link transform if necessary
        if (getTransform() == null && component instanceof Transform) {
            setTransform((Transform) component);
        }
    }

    @SuppressWarnings("unchecked")
    default <ComponentInstance extends Component> ComponentInstance remove(Class<ComponentInstance> type) {
        if (Transform.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Transform component cannot be removed from entity!");
        }
        return (ComponentInstance) getComponentMap().remove(type);
    }

    default Set<Class<? extends Component>> getComponentClassSet() {
        return Collections.unmodifiableSet(getComponentMap().keySet());
    }

}
