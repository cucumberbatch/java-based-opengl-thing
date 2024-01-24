package org.north.core.architecture;

import org.north.core.entities.Entity;
import org.north.core.components.Component;

import java.util.List;

public interface EntityManager {
    Entity createEntity(Entity parent);
    Entity createEntity(Entity parent, String name);

    void linkWithParent(Entity parent, Entity entity);

    Entity getById(long id);
    Entity getByIdFromParent(Entity parent, long id);
    Entity getByName(String name);
    Entity getByNameFromParent(Entity parent, String name);
    Entity getRoot(Entity currentEntity);
    List<Entity> getSiblings(Entity currentEntity);
    List<Entity> getByComponents(Class<Component>... componentTypes);
    boolean isParent(Entity current, Entity target);
    boolean isDaughter(Entity current, Entity target);
}
