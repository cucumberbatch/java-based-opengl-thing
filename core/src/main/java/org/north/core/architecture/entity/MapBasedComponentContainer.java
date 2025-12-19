package org.north.core.architecture.entity;

import org.north.core.component.Component;
import org.north.core.exception.ComponentAlreadyExistsException;

import java.util.*;

public class MapBasedComponentContainer implements ComponentContainer {
    private final
    Map<Class<? extends Component>, Map<Entity, ? extends Component>> componentByEntityStorage;

    private final
    Map<Class<? extends Component>, List<Component>> componentByTypeStorage;


    public MapBasedComponentContainer() {
        this.componentByEntityStorage = new HashMap<>();
        this.componentByTypeStorage   = new IdentityHashMap<>();
    }

    @Override
    public boolean has(Entity entity, Class<? extends Component> type) {
        Map<Entity, ? extends Component> componentsMap = componentByEntityStorage.get(type);
        return componentsMap == null || !componentsMap.containsKey(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Component> C get(Entity entity, Class<C> type) {
        Map<Entity, ? extends Component> componentsMap = componentByEntityStorage.get(type);
        C component;
        if (componentsMap == null || (component = (C) componentsMap.get(entity)) == null) {
            throw new NullPointerException("Component not found!");
        }
        return type.cast(component);
    }

    @Override
    public <C extends Component> void add(Entity entity, C component) {
        Class<? extends Component> type = component.getClass();
        @SuppressWarnings("unchecked")
        Map<Entity, Component> componentsMap = (Map<Entity, Component>) componentByEntityStorage.get(type);
        List<Component>        components    = componentByTypeStorage.get(type);

        if (componentsMap == null || components == null) {
            componentsMap = new HashMap<>();
            components    = new ArrayList<>();
            componentByEntityStorage.put(type, componentsMap);
            componentByTypeStorage  .put(type, components);
        } else if (componentsMap.containsKey(entity)) {
            throw new ComponentAlreadyExistsException(type);
        }

        componentsMap.put(entity, component);
        components   .add(component);
    }

    @Override
    public <C extends Component> C remove(Entity entity, Class<C> type) {
        Map<Entity, ? extends Component> componentsMap = componentByEntityStorage.get(type);

        Component component;
        if (componentsMap == null || (component = componentsMap.remove(entity)) == null) {
            throw new NullPointerException("Component not found!");
        }
        componentByTypeStorage.get(type).remove(component);
        return type.cast(component);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Component> Collection<C> getComponentsByType(Class<C> type) {
        return (Collection<C>) componentByTypeStorage.get(type);
    }

    @Override
    public Set<Class<? extends Component>> getComponentTypesSet(Entity entity) {
        Set<Class<? extends Component>> componentTypes = new HashSet<>();
        for (Map<Entity, ? extends Component> entityToComponentMap : componentByEntityStorage.values()) {
            Component component = entityToComponentMap.get(entity);
            if (component != null) {
                componentTypes.add(component.getClass());
            }
        }
        return componentTypes;
    }
}
