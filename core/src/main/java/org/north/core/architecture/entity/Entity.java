package org.north.core.architecture.entity;

import org.north.core.architecture.tree.TreeNode;
import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.component.serialization.Serializable;
import org.north.core.physics.collision.Collidable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.north.core.utils.SerializationUtils.readUUID;
import static org.north.core.utils.SerializationUtils.writeUUID;

/**
 * Entity is an object that contains a bunch of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
public class Entity extends TreeNode<Entity> implements Collidable, Serializable<ObjectOutputStream, ObjectInputStream> {
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
    public void serializeObject(ObjectOutputStream out) throws IOException {
        writeUUID(out, id);
        out.writeUTF(name);
        out.writeByte(components.size());

        for (Class<? extends Component> componentClass: components.keySet()) {
            out.writeObject(componentClass);
            Component component = components.get(componentClass);
            component.serializeObject(out);
        }
    }

    @Override
    public void deserializeObject(ObjectInputStream in) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        id = readUUID(in);
        name = in.readUTF();
        byte componentsCount = in.readByte();

        for (byte i = 0; i < componentsCount; i++) {
            Class<? extends Component> componentClass = (Class<? extends Component>) in.readObject();
            Component component = componentClass.getConstructor().newInstance();
            component.deserializeObject(in);
        }
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
