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
        linkWithParent(parent, entity);
        return entity;
    }

    @Override
    public Entity createEntity(Entity parent, String name) {
        Entity entity = new Entity(name);
        entity.id = generateId();
        if (this.root == null) {
            this.root = entity;
        }
        linkWithParent(parent, entity);
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
        if (entity.parent != null) {
            entity.parent.daughters.remove(entity);
        }
        if (entity.daughters.contains(parent)) {
            entity.daughters.remove(parent);
            parent.parent = null;
        }
        if (entity.parent != parent) {
            parent.daughters.add(entity);
        }
        if (this.root == null) {
            this.root = parent;
        }
        entity.parent = parent;
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
        Entity root = currentEntity;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    @Override
    public List<Entity> getSiblings(Entity currentEntity) {
        if (currentEntity.parent != null) {
            return currentEntity.parent.daughters;
        }
        return Collections.singletonList(currentEntity);
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
        Entity selected = current;
        while (selected.parent != null) {
            if (selected.parent == target) {
                return true;
            }
            selected = selected.parent;
        }
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
        if (!parent.daughters.isEmpty()) {
            for (Entity daughter : parent.daughters) {
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
        if (!parent.daughters.isEmpty()) {
            for (Entity daughter : parent.daughters) {
                target = this.getByNameFromParent(daughter, name);
                if (target != null) return target;
            }
        }
        return null;
    }


}
