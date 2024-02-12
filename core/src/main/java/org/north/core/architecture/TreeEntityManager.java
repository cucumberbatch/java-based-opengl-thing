package org.north.core.architecture;

import org.north.core.architecture.entity.Entity;
import org.north.core.components.Component;

import java.util.List;

public class TreeEntityManager implements EntityManager {
    private static TreeEntityManager instance;

    private Entity root;

    transient private long idSequence = 1;

    private long generateId() {
        return idSequence++;
    }

    public static TreeEntityManager getInstance() {
        if (instance == null) {
            instance = new TreeEntityManager();
        }
        return instance;
    }

    @Override
    public Entity create(String name) {
        return create(null, name);
    }

    @Override
    public Entity create(Entity parent) {
        return create(parent, null);
    }

    @Override
    public Entity create(Entity parent, String name) {
        Entity entity = new Entity(name);
        entity.id = generateId();
        updateEntityHierarchy(entity, parent);
        return entity;
    }

    private void updateEntityHierarchy(Entity entity, Entity parent) {
        if (this.root == null) {
            this.root = entity;
        } else {
            entity.setParent((parent == null) ? this.root : parent);
        }
    }

    public void setRoot(Entity root) {
        this.root = root;
    }

    @Override
    public void linkWithParent(Entity parent, Entity entity) {
        entity.setParent(parent);
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
        return currentEntity.getSiblings();
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
        return target.isParentOf(current);
    }

    @Override
    public boolean isDaughter(Entity current, Entity target) {
        return current.isParentOf(target);
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
        List<Entity> daughters = parent.getDaughters();
        if (!daughters.isEmpty()) {
            for (Entity daughter : daughters) {
                target = this.getByNameFromParent(daughter, name);
                if (target != null) return target;
            }
        }
        return null;
    }


}
