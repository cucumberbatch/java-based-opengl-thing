package org.north.core.architecture.entity;

import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.managment.SystemManager;
import org.north.core.reflection.di.Inject;
import org.north.core.system.command.AddComponentDeferredCommand;
import org.north.core.system.command.RemoveComponentDeferredCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentManager {
    private final ManagedEntityPool managedEntityPool;
    private final SystemManager systemManager;

    @Inject
    public ComponentManager(ApplicationContext context) {
        this.systemManager = context.getSystemManager();
        this.managedEntityPool = new ManagedEntityPool(this);
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

    public final <E extends Component> E add(Entity entity, Class<E> componentClass) {
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
    public final List<? extends Component> add(Entity entity, Class<? extends Component>... classes) {
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

    public final <E extends Component> E get(Entity entity, Class<E> componentClass) {
        return entity.get(componentClass);
    }

    @SafeVarargs
    public final List<? extends Component> get(Entity entity, Class<? extends Component>... classes) {
        return Arrays.stream(classes)
                .map(entity::get)
                .collect(Collectors.toList());
    }

    public final <E extends Component> E remove(Entity entity, Class<E> componentClass) {
        if (componentClass.isAssignableFrom(Transform.class)) {
            throw new IllegalArgumentException("Transform component cannot be removed!");
        }

        E component = entity.get(componentClass);
        systemManager.addDeferredCommand(new RemoveComponentDeferredCommand(entity, component));
        return component;
    }

    @SafeVarargs
    public final List<? extends Component> remove(Entity entity, Class<? extends Component>... classes) {
        return Arrays.stream(classes)
                .map(componentClass -> remove(entity, componentClass))
                .collect(Collectors.toList());
    }

    private <E extends Component> E instantiateComponent(Class<E> componentClass) {
        try {
            return componentClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
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

        public synchronized <E extends Component> E add(Class<E> componentClass) {
            cm.pushManagedEntity(this);
            return cm.add(entity, componentClass);
        }

        @SafeVarargs
        public final synchronized List<? extends Component> add(Class<? extends Component>... componentClasses) {
            cm.pushManagedEntity(this);
            return cm.add(entity, componentClasses);
        }

        public synchronized <E extends Component> E get(Class<E> componentClass) {
            cm.pushManagedEntity(this);
            return entity.get(componentClass);
        }

        @SafeVarargs
        public final synchronized List<? extends Component> get(Class<? extends Component>... componentClasses) {
            cm.pushManagedEntity(this);
            return cm.get(entity, componentClasses);
        }

        public synchronized <E extends Component> void remove(Class<E> componentClass) {
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
            // Logger.debug("Push ManagedEntity instance [" + managedEntity + "] from pool. Pool size: " + this.managedEntityDeque.size());
        }

        private ManagedEntity pop(Entity entity) {
            if (this.managedEntityDeque.isEmpty()) {
                ManagedEntity managedEntity = new ManagedEntity(entityManager, entity);
                // Logger.debug("Created ManagedEntity instance [" + managedEntity + "] for entity: " + entity.getName() + ". Pool size: " + this.managedEntityDeque.size());
                return managedEntity;
            }
            ManagedEntity managedEntity = this.managedEntityDeque.pop();
            managedEntity.setEntity(entity);
            // Logger.debug("Pop ManagedEntity instance [" + managedEntity + "] from pool for entity: " + entity.getName() + ". Pool size: " + this.managedEntityDeque.size());
            return managedEntity;
        }

    }

}
