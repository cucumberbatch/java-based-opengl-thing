package org.north.core.architecture;

import org.north.core.architecture.entity.Entity;
import org.north.core.components.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TreeEntityManager implements EntityManager {
    private long idSequence = 1;
    private Entity root;

    @Override
    public Entity create() {
        return create(null, null);
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

    private long generateId() {
        return idSequence++;
    }

    private void updateEntityHierarchy(Entity entity, Entity parent) {
        if (this.root == null) {
            this.root = entity;
        } else {
            entity.setParent((parent == null) ? this.root : parent);
        }
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
    @SafeVarargs
    public final List<Entity> getByComponents(Class<? extends Component>... componentTypes) {
        return getByComponentsFromParent(new ArrayList<>(), root, componentTypes);
    }

    @Override
    public Entity getByIdFromParent(Entity parent, long id) {
        if (parent.id == id) {
            return parent;
        }
        Entity target;
        if (!parent.getDaughters().isEmpty()) {
            for (Entity daughter: parent.getDaughters()) {
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
            for (Entity daughter: daughters) {
                target = this.getByNameFromParent(daughter, name);
                if (target != null) return target;
            }
        }
        return null;
    }

    @Override
    @SafeVarargs
    public final List<Entity> getByComponentsFromParent(List<Entity> entities, Entity parent, Class<? extends Component>... componentTypes) {
        if (parent == null) {
            return entities;
        }
        if (!parent.getDaughters().isEmpty()) {
            for (Entity daughter: parent.getDaughters()) {
                this.getByComponentsFromParent(entities, daughter, componentTypes);
                if (daughter.getComponentClassSet().equals(Set.of(componentTypes))) {
                    entities.add(parent);
                }
            }
        }
        return entities;
    }


}
