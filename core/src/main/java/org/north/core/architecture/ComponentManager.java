package org.north.core.architecture;

import org.north.core.components.Camera;
import org.north.core.components.Component;
import org.north.core.components.Transform;
import org.north.core.entities.Entity;
import org.north.core.managment.SystemManager;
import org.north.core.systems.command.AddComponentDeferredCommand;
import org.north.core.systems.command.RemoveComponentDeferredCommand;
import org.north.core.utils.Logger;

import java.lang.reflect.InvocationTargetException;

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

    public <E extends Component> E addComponent(Entity entity, Class<E> componentClass) {
        if (entity == null || componentClass == null) {
            throw new IllegalArgumentException("Entity or component class must not be null");
        }

        E component;

        try {
            //note: at this moment any component needs to have a constructor without any params
            component = componentClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // Logger.error("Error while creating a component instance: " + e.getMessage());
            throw new RuntimeException(e);
        }

        component.setId(nextId());
        systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));
        entity.addComponent(component);

        return component;
    }

    public <E extends Component> E getComponent(Entity entity, Class<E> componentClass) {
        return entity.getComponent(componentClass);
    }

    public <E extends Component> E removeComponent(Entity entity, Class<E> componentClass) {
        if (componentClass.isAssignableFrom(Transform.class)) {
            throw new IllegalArgumentException("Transform component cannot be removed!");
        }

        E component = entity.getComponent(componentClass);
        systemManager.addDeferredCommand(new RemoveComponentDeferredCommand(entity, component));
        return component;
    }
}
