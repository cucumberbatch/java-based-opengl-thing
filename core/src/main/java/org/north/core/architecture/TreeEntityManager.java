package org.north.core.architecture;

import org.north.core.entities.Entity;
import org.north.core.components.Component;

import java.util.Collections;
import java.util.List;

public class TreeEntityManager implements EntityManager {
    private static TreeEntityManager instance;

    private Entity root;

    transient private long idSequence = 1;

    private long generateId() {
        return idSequence++;
    }

    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new TreeEntityManager();
        }
        return instance;
    }

    @Override
    public Entity createEntity(Entity parent) {
        Entity entity = new Entity();
        entity.id = generateId();
        if (this.root == null) {
            this.root = entity;
        }
        entity.setParent(parent);
        return entity;
    }

    @Override
    public Entity createEntity(Entity parent, String name) {
        Entity entity = new Entity(name);
        entity.id = generateId();
        if (this.root == null) {
            this.root = entity;
        }
        entity.setParent(parent);
        return entity;
    }

    // todo: реализовать проверки на образование неявного цикла при связывании
    // например, связывание 1 в качестве предка для 4, и 2 в качестве родителя для 4:
    //       1      и     4
    //      / \
    //     2   3
    //
    //
    //       1
    //      / \
    //     2   3
    //    /
    //   4
    //    \
    //     1  <- на данный момент возможна такая привязка,
    //    / \     которая при вызове метода получения корня образует бесконечный цикл
    //  ..  ..
    @Override
    public void linkWithParent(Entity parent, Entity entity) {
    }

    @Override
    public Entity getById(long id) {
        return getByIdFromParent(this.root, id);
    }

    @Override
    public Entity getByName(String name) {
        return getByNameFromParent(this.root, name);
    }

    @Override
    public Entity getRoot(Entity currentEntity) {
        return currentEntity.getRoot();
    }

    @Override
    public List<Entity> getSiblings(Entity currentEntity) {
        return Collections.emptyList();
    }

    @Override
    public List<Entity> getByComponents(Class<Component>... componentTypes) {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Checks if target entity is a parent for a current entity
     */
    @Override
    public boolean isParent(Entity current, Entity target) {
        return false;
    }

    @Override
    public boolean isDaughter(Entity current, Entity target) {
        return false;
    }

    @Override
    public Entity getByIdFromParent(Entity parent, long id) {
        if (parent.id == id) {
            return parent;
        }
        Entity target;
        if (!parent.getDaughters().isEmpty()) {
            for (Entity daughter : parent.getDaughters()) {
                target = this.getByIdFromParent(daughter, id);
                if (target != null) return target;
            }
        }
        return null;
    }

    @Override
    public Entity getByNameFromParent(Entity parent, String name) {
        if (parent.name.equals(name)) {
            return parent;
        }
        Entity target;
        if (!parent.getDaughters().isEmpty()) {
            for (Entity daughter : parent.getDaughters()) {
                target = this.getByNameFromParent(daughter, name);
                if (target != null) return target;
            }
        }
        return null;
    }


}
