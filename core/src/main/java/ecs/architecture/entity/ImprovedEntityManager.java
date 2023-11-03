package ecs.architecture.entity;

import ecs.architecture.ComponentManager;
import ecs.components.Component;
import ecs.entities.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImprovedEntityManager {

    private final ComponentManager componentManager;

    public ImprovedEntityManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    public Entity create() {
        return new Entity();
    }

    public Entity create(String name) {
        return new Entity(name);
    }

    public ManagedEntity take(Entity entity) {
        return new ManagedEntity(this, entity);
    }

    public class ManagedEntity {
        private final ImprovedEntityManager entityManager;
        private final Entity entity;

        public ManagedEntity(ImprovedEntityManager entityManager, Entity entity) {
            this.entityManager = entityManager;
            this.entity = entity;
        }

        public synchronized <E extends Component> E add(Class<E> componentClass) {
            return componentManager.addComponent(entity, componentClass);
        }

        @SafeVarargs
        public final synchronized List<Component> add(Class<? extends Component>... componentClasses) {
            return Arrays.stream(componentClasses)
                    .map(type -> componentManager.addComponent(entity, type))
                    .collect(Collectors.toList());
        }

        public synchronized <E extends Component> E get(Class<E> componentClass) {
            return entity.getComponent(componentClass);
        }

        @SafeVarargs
        public final synchronized List<Component> get(Class<? extends Component>... componentClasses) {
            return Arrays.stream(componentClasses)
                    .map(entity::getComponent)
                    .collect(Collectors.toList());
        }

        public synchronized <E extends Component> void remove(Class<E> componentClass) {
            componentManager.removeComponent(entity, componentClass);
        }

        @SafeVarargs
        public final synchronized void remove(Class<? extends Component>... componentClasses) {
            Arrays.stream(componentClasses)
                    .forEach(type -> componentManager.removeComponent(entity, type));
        }

    }


    // I don't remember why I put this class...
    private class ManagedComponent<E extends Component> {

        public ManagedComponent(E component) {

        }
    }
}
