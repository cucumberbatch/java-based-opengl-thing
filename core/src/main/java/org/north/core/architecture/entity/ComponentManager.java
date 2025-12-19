package org.north.core.architecture.entity;

import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.management.SystemManager;
import org.north.core.reflection.di.Inject;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class ComponentManager {
    private final SystemManager      systemManager;
    private final ComponentContainer container;

    @Inject
    public ComponentManager(ApplicationContext context) {
        try {
            this.systemManager = context.getDependency(SystemManager.class);
            this.container     = context.addDependency(ComponentContainer.class, new MapBasedComponentContainer());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCameraComponent(Camera camera) {
        systemManager.setCameraComponent(camera);
    }

    public final <C extends Component> C add(Entity entity, Class<C> type) {
        if (entity == null || type == null) {
            throw new IllegalArgumentException("Entity or component class must not be null");
        }

        C component = instantiateComponentOfType(type);
        component.setEntity(entity);

        // temporary disabled deferred commands for a while
        //systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));

        // using system manager instead for instant linking of entities with components
        systemManager.addComponent(component);
        container.add(entity, component);

        return component;
    }

    @SafeVarargs
    public final List<? extends Component> add(Entity entity, Class<? extends Component>... types) {
        if (entity == null || types == null) {
            throw new IllegalArgumentException("Entity or component types must not be null or empty");
        }

        List<Component> components = new ArrayList<>(types.length);
        for (Class<? extends Component> type : types) {
            Component component = instantiateComponentOfType(type);
            component.setEntity(entity);
            components.add(component);

            // temporary disabled deferred commands for a while
            //systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));

            // using system manager instead for instant linking of entities with components
            systemManager.addComponent(component);
            container.add(entity, component);
        }
        return components;
    }

    public final <C extends Component> C addAndPerform(Entity entity, Class<C> type, Consumer<C> action) {
        C component = add(entity, type);
        action.accept(component);
        return component;
    }

    public final <C extends Component> C get(Entity entity, Class<C> type) {
        return container.get(entity, type);
    }

    @SafeVarargs
    public final List<? extends Component> get(Entity entity, Class<? extends Component>... types) {
        List<Component> components = new ArrayList<>(types.length);
        for (Class<? extends Component> type : types) {
            Component component = container.get(entity, type);
            components.add(component);
        }
        return components;
    }

    public final boolean has(Entity entity, Class<? extends Component> type) {
        return container.has(entity, type);
    }

    public final <C extends Component> C remove(Entity entity, Class<C> type) {
        if (type.isAssignableFrom(Transform.class)) {
            throw new IllegalArgumentException("Transform component cannot be removed!");
        }

        C component = container.get(entity, type);

        //systemManager.addDeferredCommand(new RemoveComponentDeferredCommand(entity, component));

        component.setActivity(false);
        systemManager.getSystemByComponentType(type).removeComponent(component);


        return component;
    }

    @SafeVarargs
    public final List<? extends Component> remove(Entity entity, Class<? extends Component>... types) {
        List<Component> components = new ArrayList<>();
        for (Class<? extends Component> type : types) {
            Component remove = remove(entity, type);
            components.add(remove);
        }
        return components;
    }

    public final <C extends Component> Collection<C> getAllByType(Class<C> type) {
        return container.getComponentsByType(type);
    }

    private <C extends Component> C instantiateComponentOfType(Class<C> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            String message = String.format(
                    "Failed to instantiate component of type '%s' using an empty constructor.", type
            );
            throw new RuntimeException(message, e);
        }
    }

}
