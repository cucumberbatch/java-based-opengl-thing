package org.north.core.architecture.entity;

import org.north.core.architecture.tree.TreeNode;
import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.physics.collision.Collidable;

import java.io.*;
import java.util.*;

/**
 * Entity is an object that contains a bunch of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
public class Entity extends TreeNode<Entity> implements Collidable, Externalizable {
    public UUID id;
    public String name;

    public Map<Class<? extends Component>, Component> components;
    public Transform transform;

    public Entity() {
        this(null);
    }

    public Entity(String name) {
        UUID id = UUID.randomUUID();
        this.id = id;
        this.name = Objects.requireNonNullElse(name, id.toString());
        this.components = new HashMap<>(4, 1.0f);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addComponent(Component component) {
        components.put(component.getClass(), component);
        component.setEntity(this);

        // link transform if necessary
        if (this.transform == null && component instanceof Transform) {
            this.transform = (Transform) component;
        }
    }

    @SuppressWarnings("unchecked")
    public <E extends Component> E get(Class<E> clazz) {
        return (E) components.get(clazz);
    }

    public Set<Class<? extends Component>> getComponentClassSet() {
        return Collections.unmodifiableSet(components.keySet());
    }

    @SuppressWarnings("unchecked")
    public <E extends Component> E removeComponent(Class<E> clazz) {
        if (Transform.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Transform component cannot be removed from entity!");
        }
        return (E) components.remove(clazz);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(id);
        out.writeUTF(name);
        out.writeObject(transform);
        out.writeObject(components);
        out.writeObject(parent);
        out.writeObject(daughters);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = (UUID) in.readObject();
        name = in.readUTF();
        transform = (Transform) in.readObject();
        components = (Map<Class<? extends Component>, Component>) in.readObject();
        parent = (Entity) in.readObject();
        daughters = (List<Entity>) in.readObject();
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", daughters=" + daughters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
