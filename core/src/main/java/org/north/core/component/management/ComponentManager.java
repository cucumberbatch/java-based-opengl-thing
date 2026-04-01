package org.north.core.component.management;

import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.entity.Entity;
import org.north.core.management.SystemManager;
import org.north.core.system.command.AddComponentDeferredCommand;
import org.north.core.system.command.RemoveComponentDeferredCommand;
import org.north.core.reflection.di.Inject;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ComponentManager {
    private final SystemManager      systemManager;
    private final ComponentContainer container;

    @Inject
    public ComponentManager(ApplicationContext context) {     
        this.container     = context.getDependency(ComponentContainer.class);
        this.systemManager = context.getDependency(SystemManager.class);
    }

    public void setCameraComponent(Camera camera) {
        systemManager.setCameraComponent(camera);
    }

    public <C extends Component> C add(Entity entity, C component) {
        if (entity == null || component == null) {
            throw new IllegalArgumentException("Entity or component must not be null");
        }
        return registerComponent(entity, component);        
    }

    public final <C extends Component> C add(Entity entity, Class<C> type) {
        if (entity == null || type == null) {
            throw new IllegalArgumentException("Entity or component class must not be null");
        }
        return instantiateAndRegisterComponent(entity, type);
    }

    @SafeVarargs
    public final List<? extends Component> add(Entity entity, Class<? extends Component>... types) {
        if (entity == null || types == null) {
            throw new IllegalArgumentException("Entity or component types must not be null or empty");
        }
        List<Component> addedComponents = new ArrayList<>(types.length);
        for (Class<? extends Component> type : types) {
            addedComponents.add(instantiateAndRegisterComponent(entity, type));
        }
        return addedComponents;
    }

    // public final <C extends Component> C addAndPerform(Entity entity, Class<C> type, Consumer<C> action) {
    //     C component = add(entity, type);
    //     action.accept(component);
    //     return component;
    // }

    public final <C extends Component> C get(Entity entity, Class<C> type) {
        return container.get(entity, type);
    }

    @SafeVarargs
    public final List<? extends Component> get(Entity entity, Class<? extends Component>... types) {
        List<Component> foundComponents = new ArrayList<>(types.length);
        for (Class<? extends Component> type : types) {
            Component component = container.get(entity, type);
            foundComponents.add(component);
        }
        return foundComponents;
    }

    public final boolean has(Entity entity, Class<? extends Component> type) {
        return container.has(entity, type);
    }

    public final <C extends Component> C remove(Entity entity, Class<C> type) {
        if (type.isAssignableFrom(Transform.class)) {
            throw new IllegalArgumentException("Transform component cannot be removed!");
        }

        C component = container.get(entity, type);

        systemManager.addDeferredCommand(new RemoveComponentDeferredCommand(entity, component));

        //component.setActivity(false);
        //systemManager.getSystemByComponentType(type).removeComponent(component);

        return component;
    }

    @SafeVarargs
    public final List<? extends Component> remove(Entity entity, Class<? extends Component>... types) {
        List<Component> removedComponents = new ArrayList<>();
        for (Class<? extends Component> type : types) {
            Component remove = remove(entity, type);
            removedComponents.add(remove);
        }
        return removedComponents;
    }

    public final <C extends Component> Collection<C> getAllByType(Class<C> type) {
        return container.getComponentsByType(type);
    }

    private <C extends Component> C instantiateAndRegisterComponent(Entity entity, Class<C> componentType) {
        C component = instantiateComponentOfType(componentType);
        return registerComponent(entity, component);
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

    private <C extends Component> C registerComponent(Entity entity, C component) {
        component.setEntity(entity);
        systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));
        return component;
    }

}
