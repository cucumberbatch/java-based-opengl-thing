package org.north.core.architecture.tree;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.Component;

import java.util.*;

public class EntityTree extends AbstractCollection<Entity> implements Tree<Entity> {
    private Entity root;
    private int size;

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

        if (this.root == null) {
            this.root = entity;
        } else {
            entity.setParent((parent == null) ? this.root : parent);
        }
        size++;

        return entity;
    }

    @Override
    public Entity getById(UUID id) {
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
    public Entity getByIdFromParent(Entity parent, UUID id) {
        if (id.equals(parent.id)) {
            return parent;
        }
        Entity target;
        List<Entity> daughters = parent.getDaughters();
        if (!daughters.isEmpty()) {
            for (Entity daughter: daughters) {
                target = this.getByIdFromParent(daughter, id);
                if (target != null) return target;
            }
        }
        return null;
    }

    @Override
    public Entity getByNameFromParent(Entity parent, String name) {
        if (name.equals(parent.name)) {
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
        List<Entity> daughters = parent.getDaughters();
        if (!daughters.isEmpty()) {
            for (Entity daughter: daughters) {
                this.getByComponentsFromParent(entities, daughter, componentTypes);
                if (daughter.getComponentClassSet().equals(Set.of(componentTypes))) {
                    entities.add(parent);
                }
            }
        }
        return entities;
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityTreeIterator(this.root, size);
    }

    @Override
    public int size() {
        return size;
    }

    public static class EntityTreeIterator implements Iterator<Entity> {
        private final Entity[] entities;
        private int index;

        EntityTreeIterator(Entity entity, int count) {
            entities = new Entity[count];
            traverseTree(entity);
            index = 0;
        }

        private Entity traverseTree(Entity parent) {
            if (parent.getDaughters().isEmpty()) {
                return parent;
            }
            for (Entity daughter : parent.getDaughters()) {
                Entity target = traverseTree(daughter);
                if (target != null) {
                    entities[index++] = target;
                }
            }
            entities[index++] = parent;
            return null;
        }

        @Override
        public boolean hasNext() {
            return index < entities.length;
        }

        @Override
        public Entity next() {
            return entities[index++];
        }
    }


}
