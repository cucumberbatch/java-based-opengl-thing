package org.north.core.architecture.tree;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.Component;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

public class EntityTree extends AbstractCollection<Entity> implements Tree<Entity>, Externalizable {
    public static final Entity EMPTY_ENTITY;

    static {
        EMPTY_ENTITY = new Entity();
        EMPTY_ENTITY.setId(new UUID(0, 0));
    }

    private Entity root;
    private int size;

    @Override
    public boolean add(Entity entity) {
        if (root == null) {
            root = entity;
        } else {
            entity.setParent(root);
        }
        int count = 0;
        Iterator<Entity> iterator = new EntityTreeIterator(entity);
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        this.size += count;
        return true;
    }

    public boolean add(Entity parent, Entity daughter) {
        if (root == null) {
            return false;
        }
        if (daughter == null) {
            return false;
        }
        if (!root.isAncestorOf(parent)) {
            return false;
        }
        daughter.setParent(parent);
        int count = 0;
        Iterator<Entity> iterator = new EntityTreeIterator(daughter);
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        this.size += count;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Entity)) {
            return false;
        }
        if (root == null) {
            return false;
        }
        Entity entity = (Entity) o;
        if (entity.equals(root)) {
            root = null;
            size = 0;
            return true;
        }
        Entity parent = entity.getParent();
        if (parent != null) {
            entity.setParent(null);
            return true;
        }
        return false;
    }

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

        if (root == null) {
            root = entity;
        } else {
            entity.setParent((parent == null) ? this.root : parent);
        }

        int count = 0;
        for (Entity value : this) {
            count++;
        }
        this.size = count;

        return entity;
    }

    public void updateFrom(EntityTree tree) {
        this.root = tree.root;
        this.size = tree.size;
    }

    @Override
    public Entity getRoot() {
        return root;
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
        for (Entity daughter: parent.getDaughters()) {
            Entity target = this.getByIdFromParent(daughter, id);
            if (target != null) return target;
        }
        return null;
    }

    @Override
    public Entity getByNameFromParent(Entity parent, String name) {
        if (parent == null) {
            return null;
        }
        if (name.equals(parent.name)) {
            return parent;
        }
        for (Entity daughter: parent.getDaughters()) {
            Entity target = this.getByNameFromParent(daughter, name);
            if (target != null) return target;
        }
        return null;
    }

    @Override
    @SafeVarargs
    public final List<Entity> getByComponentsFromParent(List<Entity> entities, Entity parent, Class<? extends Component>... componentTypes) {
        if (parent == null) {
            return entities;
        }
        Collection<Entity> daughters = parent.getDaughters();
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
        return new EntityTreeIterator(this.root);
    }

    @Override
    public String toString() {
        return "EntityTree{" +
                "root=" + root +
                ", size=" + size +
                '}';
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(root);
        out.writeInt(size);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        root = (Entity) in.readObject();
        size = in.readInt();
    }

    public static class EntityTreeIterator implements Iterator<Entity> {
        private final List<Entity> entities;
        private int count;

        EntityTreeIterator(Entity entity) {
            entities = new ArrayList<>();
            traverseTree(entity);
            count = entities.size();
        }

        private void traverseTree(Entity parent) {
            if (parent == null) return;
            List<Entity> daughters = (List<Entity>) parent.getDaughters();
            for (int i = 0; i < daughters.size(); i++) {
                Entity daughter = daughters.get(i);
                traverseTree(daughter);
            }
            entities.add(parent);
        }

        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public Entity next() {
            return entities.get(entities.size() - (count--));
        }
    }


}
