package ecs.architecture;

import ecs.entities.Entity;
import ecs.components.Component;

import java.util.List;

public interface EntityManager {
    Entity createEntity();

    void linkWithParent(Entity parent, Entity entity);

    Entity getById(Entity parent, long id);
    Entity getByName(Entity parent, String name);
    Entity getByIdFromParent(Entity parent, long id);
    Entity getByNameFromParent(Entity parent, String name);
    Entity getRoot(Entity currentEntity);
    List<Entity> getSiblings(Entity currentEntity);
    List<Entity> getByComponents(Class<Component>... componentTypes);
    boolean isParent(Entity current, Entity target);
    boolean isDaughter(Entity current, Entity target);
}
