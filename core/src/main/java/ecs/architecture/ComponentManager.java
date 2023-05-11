package ecs.architecture;

import ecs.components.Component;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.managment.SystemManager;

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

    public void setDataManagers(EntityManager entityManager, SystemManager systemManager) {
        this.entityManager = entityManager;
        this.systemManager = systemManager;
    }

    private void generateComponentId(Component component) {
        if (component.getId() == 0) component.setId(idCounter++);
    }

    private <E extends Component> void setupTransforms(Entity entity, E component) {
        if (entity.transform == null) {
            Transform transform = entity.getComponent(Transform.class);
            if (transform == null && component instanceof Transform) {
                entity.transform = (Transform) component;
            } else {
                component.setTransform(transform);
            }
        } else {
            component.setTransform(entity.transform);
        }
    }

    public <E extends Component> void addComponent(Entity entity, E component) {
        generateComponentId(component);
        systemManager.addComponent(component);
        entity.addComponent(component);
        setupTransforms(entity, component);
    }

    public <E extends Component> E getComponent(Entity entity, Class<E> componentClass) {
        return entity.getComponent(componentClass);
    }

    public <E extends Component> E removeComponent(Entity entity, Class<E> componentClass) {
        if (componentClass.isAssignableFrom(Transform.class)) {
            throw new IllegalArgumentException("Transform component cannot be removed!");
        }
        E component = entity.getComponent(componentClass);
        return (E) systemManager.systemMap.get(componentClass).removeComponent(component.getId());
    }
}
