package org.north.core.architecture.entity;

import org.north.core.component.Component;
import org.north.core.exception.ComponentAlreadyExistsException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapBasedComponentContainer implements ComponentContainer {
    private final Map<Class<? extends Component>, Map<Entity, ? extends Component>> componentStorage = new HashMap<>();

    @Override
    public boolean has(Entity entity, Class<? extends Component> type) {
        Map<Entity, ? extends Component> entityComponents = componentStorage.get(type);
        return entityComponents == null || !entityComponents.containsKey(entity);
    }

    @Override
    public <C extends Component> C get(Entity entity, Class<C> type) {
        Map<Entity, ? extends Component> entityComponents = componentStorage.get(type);
        if (entityComponents == null || !entityComponents.containsKey(entity)) {
            throw new NullPointerException("Component not found!");
        }
        return type.cast(entityComponents.get(entity));
    }

    @Override
    public <C extends Component> void add(Entity entity, C component) {
        Class<? extends Component> type = component.getClass();
        @SuppressWarnings("unchecked") Map<Entity, Component> entityComponents =
                (Map<Entity, Component>) componentStorage.get(type);

        if (entityComponents == null) {
            entityComponents = new HashMap<>();
            componentStorage.put(type, entityComponents);
        } else if (entityComponents.containsKey(entity)) {
            throw new ComponentAlreadyExistsException(type);
        }
        entityComponents.put(entity, component);
    }

    @Override
    public <C extends Component> C remove(Entity entity, Class<C> type) {
        Map<Entity, ? extends Component> entityComponents = componentStorage.get(type);
        if (entityComponents == null || !entityComponents.containsKey(entity)) {
            throw new NullPointerException("Component not found!");
        }
        return type.cast(entityComponents.remove(entity));
    }

    @Override
    public Set<Class<? extends Component>> getComponentClassSet(Entity entity) {
        Set<Class<? extends Component>> componentTypes = new HashSet<>();
        for (Map<Entity, ? extends Component> entityToComponentMap : componentStorage.values()) {
            Component component = entityToComponentMap.get(entity);
            if (component != null) {
                componentTypes.add(component.getClass());
            }
        }
        return componentTypes;
    }
}
