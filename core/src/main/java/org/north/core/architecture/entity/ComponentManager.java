package org.north.core.architecture.entity;

import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.management.SystemManager;
import org.north.core.reflection.di.Inject;
import org.north.core.system.command.AddComponentDeferredCommand;
import org.north.core.system.command.RemoveComponentDeferredCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class ComponentManager {
    private final ManagedEntityPool managedEntityPool;
    private final SystemManager systemManager;
    private final ComponentContainer componentContainer;

    @Inject
    public ComponentManager(ApplicationContext context) {
        this.systemManager = context.getDependency(SystemManager.class);
        this.managedEntityPool = new ManagedEntityPool(this);
        try {
            this.componentContainer = context.addDependency(ComponentContainer.class, new MapBasedComponentContainer());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public ComponentContainer getComponentContainer() {
        return this.componentContainer;
    }

    public ManagedEntity take(Entity entity) {
        return popManagedEntity(entity);
    }

    public void setCameraComponent(Camera camera) {
        systemManager.setCameraComponent(camera);
    }

    private void pushManagedEntity(ManagedEntity managedEntity) {
        this.managedEntityPool.push(managedEntity);
    }

    private ManagedEntity popManagedEntity(Entity entity) {
        return this.managedEntityPool.pop(entity);
    }

    private UUID nextId() {
        return UUID.randomUUID();
    }

    public final <ComponentInstance extends Component> ComponentInstance add(Entity entity,
                                                                             Class<ComponentInstance> componentClass) {
        if (entity == null || componentClass == null) {
            throw new IllegalArgumentException("Entity or component class must not be null");
        }

        ComponentInstance component = instantiateComponent(componentClass);
        component.setId(nextId());
        component.attachToEntity(entity);
//        entity.add(component);
        systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));

        return component;
    }

    @SafeVarargs
    public final List<? extends Component> add(Entity entity,
                                               Class<? extends Component>... classes) {
        if (entity == null || classes == null) {
            throw new IllegalArgumentException("Entity or component classes must not be null or empty");
        }

        List<Component> components = new ArrayList<>();

        for (Class<? extends Component> componentClass: classes) {
            Component component = instantiateComponent(componentClass);
            component.setId(nextId());
            component.attachToEntity(entity);
            components.add(component);
            systemManager.addDeferredCommand(new AddComponentDeferredCommand(entity, component));
        }

        return components;
    }

    public final <ComponentInstance extends Component> ComponentInstance get(Entity entity,
                                                                             Class<ComponentInstance> componentClass) {
        return entity.get(entity, componentClass);
    }

    @SafeVarargs
    public final List<? extends Component> get(Entity entity, Class<? extends Component>... classes) {
        List<Component> components = new ArrayList<>();
        for (Class<? extends Component> aClass : classes) {
            Component component = entity.get(entity, aClass);
            components.add(component);
        }
        return components;
    }

    public final <ComponentInstance extends Component> ComponentInstance remove(Entity entity,
                                                                                Class<ComponentInstance> componentClass) {
        if (componentClass.isAssignableFrom(Transform.class)) {
            throw new IllegalArgumentException("Transform component cannot be removed!");
        }

        ComponentInstance component = entity.get(entity, componentClass);
        systemManager.addDeferredCommand(new RemoveComponentDeferredCommand(entity, component));
        return component;
    }

    @SafeVarargs
    public final List<? extends Component> remove(Entity entity, Class<? extends Component>... classes) {
        List<Component> components = new ArrayList<>();
        for (Class<? extends Component> componentClass : classes) {
            Component remove = remove(entity, componentClass);
            components.add(remove);
        }
        return components;
    }

    private <ComponentInstance extends Component> ComponentInstance instantiateComponent(Class<ComponentInstance> componentClass) {
        try {
            return componentClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format("Failed to instantiate component of %s through reflections", componentClass), e);
        }
    }

    public static class ManagedEntity {
        private final ComponentManager cm;
        private Entity entity;

        public ManagedEntity(ComponentManager cm, Entity entity) {
            this.cm = cm;
            this.entity = entity;
        }

        private void setEntity(Entity entity) {
            this.entity = entity;
        }

        public synchronized <ComponentInstance extends Component> ComponentInstance add(Class<ComponentInstance> componentClass) {
            cm.pushManagedEntity(this);
            return cm.add(entity, componentClass);
        }

        public synchronized <ComponentInstance extends Component> ComponentInstance addAndPerform(Class<ComponentInstance> componentClass,
                                                                                                  Consumer<ComponentInstance> action) {
            cm.pushManagedEntity(this);
            ComponentInstance component = cm.add(entity, componentClass);
            action.accept(component);
            return component;
        }

        @SafeVarargs
        public final synchronized List<? extends Component> add(Class<? extends Component>... componentClasses) {
            cm.pushManagedEntity(this);
            return cm.add(entity, componentClasses);
        }

        public synchronized <ComponentInstance extends Component> ComponentInstance get(Class<ComponentInstance> componentClass) {
            cm.pushManagedEntity(this);
            return entity.get(entity, componentClass);
        }

        @SafeVarargs
        public final synchronized List<? extends Component> get(Class<? extends Component>... componentClasses) {
            cm.pushManagedEntity(this);
            return cm.get(entity, componentClasses);
        }

        public synchronized <ComponentInstance extends Component> void remove(Class<ComponentInstance> componentClass) {
            cm.pushManagedEntity(this);
            cm.remove(entity, componentClass);
        }

        @SafeVarargs
        public final synchronized List<? extends Component> remove(Class<? extends Component>... componentClasses) {
            cm.pushManagedEntity(this);
            return cm.remove(entity, componentClasses);
        }

    }

    private static class ManagedEntityPool {
        private final ComponentManager entityManager;
        private final Deque<ManagedEntity> managedEntityDeque;

        private ManagedEntityPool(ComponentManager entityManager) {
            this.entityManager = entityManager;
            this.managedEntityDeque = new ArrayDeque<>();
        }

        private void push(ManagedEntity managedEntity) {
            this.managedEntityDeque.push(managedEntity);
        }

        private ManagedEntity pop(Entity entity) {
            if (this.managedEntityDeque.isEmpty()) {
                return new ManagedEntity(entityManager, entity);
            }
            ManagedEntity managedEntity = this.managedEntityDeque.pop();
            managedEntity.setEntity(entity);
            return managedEntity;
        }

    }

}
