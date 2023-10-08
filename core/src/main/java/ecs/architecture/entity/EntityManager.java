package ecs.architecture.entity;

import ecs.components.Component;

public class EntityManager {

    public EntityManager() {

    }

    public ManagedEntity forEntity(ecs.entities.Entity entity) {
        return new ManagedEntity(this, entity);
    }


    public class ManagedEntity {
        private final EntityManager entityManager;
        private final ecs.entities.Entity entity;

        public ManagedEntity(EntityManager entityManager, ecs.entities.Entity entity) {
            this.entityManager = entityManager;
            this.entity = entity;
        }

        public synchronized <E extends Component> E addComponent(Class<E> componentClass) {
            // do something...

            return null;
        }


    }

    private class ManagedComponent<E extends Component> {

        public ManagedComponent(E component) {

        }
    }
}
