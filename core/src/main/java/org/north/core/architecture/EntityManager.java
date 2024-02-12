package org.north.core.architecture;

import org.north.core.architecture.entity.Entity;
import org.north.core.components.Component;

import java.util.List;

public interface EntityManager {
    Entity create(String name);
    Entity create(Entity parent);
    Entity create(Entity parent, String name);

    void linkWithParent(Entity parent, Entity entity);

    Entity getById(long id);
    Entity getByIdFromParent(Entity parent, long id);
    Entity getByName(String name);
    Entity getByNameFromParent(Entity parent, String name);
    List<Entity> getByComponents(Class<? extends Component>... componentTypes);
    List<Entity> getByComponentsFromParent(List<Entity> entities, Entity parent, Class<? extends Component>... componentTypes);
    Entity getRoot(Entity currentEntity);
    List<Entity> getSiblings(Entity currentEntity);
    boolean isParent(Entity current, Entity target);
    boolean isDaughter(Entity current, Entity target);
}
