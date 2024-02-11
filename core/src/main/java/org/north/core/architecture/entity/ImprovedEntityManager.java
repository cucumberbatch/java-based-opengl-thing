package org.north.core.architecture.entity;

import org.north.core.architecture.ComponentManager;
import org.north.core.architecture.TreeEntityManager;
import org.north.core.components.Component;

import java.util.*;
import java.util.stream.Collectors;

public class ImprovedEntityManager {

    private final ManagedEntityPool managedEntityPool;
    private final TreeEntityManager entityManager;
    private final ComponentManager componentManager;

    private Entity root;

    public ImprovedEntityManager(TreeEntityManager entityManager, ComponentManager componentManager) {
        this.entityManager = entityManager;
        this.componentManager = componentManager;
        this.managedEntityPool = new ManagedEntityPool(this);
    }

    public Entity create() {
        return entityManager.createEntity(null);
    }

    public Entity create(String name) {
        return entityManager.createEntity(null, name);
    }

    public ManagedEntity take(Entity entity) {
        return popManagedEntity(entity);
    }

    private void pushManagedEntity(ManagedEntity managedEntity) {
        this.managedEntityPool.push(managedEntity);
    }

    private ManagedEntity popManagedEntity(Entity entity) {
        return this.managedEntityPool.pop(entity);
    }

    public class ManagedEntity {
        private final ImprovedEntityManager entityManager;
        private Entity entity;

        public ManagedEntity(ImprovedEntityManager entityManager, Entity entity) {
            this.entityManager = entityManager;
            this.entity = entity;
        }

        private void setEntity(Entity entity) {
            this.entity = entity;
        }

        public synchronized <E extends Component> E add(Class<E> componentClass) {
            entityManager.pushManagedEntity(this);
            return componentManager.addComponent(entity, componentClass);
        }

        @SafeVarargs
        public final synchronized List<Component> add(Class<? extends Component>... componentClasses) {
            entityManager.pushManagedEntity(this);
            return Arrays.stream(componentClasses)
                    .map(type -> componentManager.addComponent(entity, type))
                    .collect(Collectors.toList());
        }

        public synchronized <E extends Component> E get(Class<E> componentClass) {
            entityManager.pushManagedEntity(this);
            return entity.getComponent(componentClass);
        }

        @SafeVarargs
        public final synchronized List<Component> get(Class<? extends Component>... componentClasses) {
            entityManager.pushManagedEntity(this);
            return Arrays.stream(componentClasses)
                    .map(entity::getComponent)
                    .collect(Collectors.toList());
        }

        public synchronized <E extends Component> void remove(Class<E> componentClass) {
            entityManager.pushManagedEntity(this);
            componentManager.removeComponent(entity, componentClass);
        }

        @SafeVarargs
        public final synchronized void remove(Class<? extends Component>... componentClasses) {
            entityManager.pushManagedEntity(this);
            Arrays.stream(componentClasses)
                    .forEach(type -> componentManager.removeComponent(entity, type));
        }

    }

    private class ManagedEntityPool {
        private final ImprovedEntityManager entityManager;
        private final Deque<ManagedEntity> managedEntityDeque;

        private ManagedEntityPool(ImprovedEntityManager entityManager) {
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
