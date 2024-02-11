package org.north.core.architecture;

import org.north.core.components.Camera;
import org.north.core.components.Component;
import org.north.core.components.Transform;
import org.north.core.architecture.entity.Entity;
import org.north.core.managment.SystemManager;
import org.north.core.systems.command.AddComponentDeferredCommand;
import org.north.core.systems.command.RemoveComponentDeferredCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentManager {
    private static ComponentManager instance;
    private int idCounter = 0;

    private EntityManager entityManager;
    private SystemManager systemManager;

    private ComponentManager() {}

    public static ComponentManager getInstance() {
        if (instance == null) {
            instance = new ComponentManager();
        }
        return instance;
    }

    public void setCameraComponent(Camera camera) {
        systemManager.setCameraComponent(camera);
    }

    public void setDataManagers(EntityManager entityManager, SystemManager systemManager) {
        this.entityManager = entityManager;
        this.systemManager = systemManager;
    }

    private int nextId() {
        return idCounter++;
    }

    public final <E extends Component> E addComponent(Entity entity, Class<E> componentClass) {
        if (entity == null || componentClass == null) {
            throw new IllegalArgumentException("Entity or component class must not be null");
        }

        E component = instantiateComponent(componentClass);
        component.setId(nextId());
        systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));
        entity.addComponent(component);

        return component;
    }

    @SafeVarargs
    public final List<? extends Component> addComponents(Entity entity, Class<? extends Component>... classes) {
        if (entity == null || classes == null) {
            throw new IllegalArgumentException("Entity or component classes must not be null or empty");
        }

        List<Component> components = new ArrayList<>();

        for (Class<? extends Component> componentClass: classes) {
            Component component = instantiateComponent(componentClass);
            component.setId(nextId());
            systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));
            entity.addComponent(component);
            components.add(component);
        }

        return components;
    }

    public final <E extends Component> E getComponent(Entity entity, Class<E> componentClass) {
        return entity.getComponent(componentClass);
    }

    @SafeVarargs
    public final List<? extends Component> getComponents(Entity entity, Class<? extends Component>... classes) {
        return Arrays.stream(classes)
                .map(entity::getComponent)
                .collect(Collectors.toList());
    }

    public final <E extends Component> E removeComponent(Entity entity, Class<E> componentClass) {
        if (componentClass.isAssignableFrom(Transform.class)) {
            throw new IllegalArgumentException("Transform component cannot be removed!");
        }

        E component = entity.getComponent(componentClass);
        systemManager.addDeferredCommand(new RemoveComponentDeferredCommand(entity, component));
        return component;
    }

    @SafeVarargs
    public final List<? extends Component> removeComponents(Entity entity, Class<? extends Component>... classes) {
        return Arrays.stream(classes)
                .map(componentClass -> removeComponent(entity, componentClass))
                .collect(Collectors.toList());
    }

    private <E extends Component> E instantiateComponent(Class<E> componentClass) {
        try {
            return componentClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
