package org.north.core.architecture.entity;

import org.north.core.architecture.tree.TreeNode;
import org.north.core.components.Component;
import org.north.core.components.Transform;
import org.north.core.physics.Collidable;

import java.util.*;

/**
 * Entity is an object that contains a bunch of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
public class Entity extends TreeNode<Entity> implements Collidable {
    public long id;
    public String name;

    public Map<Class<? extends Component>, Component> components;
    public Transform transform;

    public Entity() {
        this(null);
    }

    public Entity(String name) {
        this.id = -1;
        this.name = Objects.requireNonNullElse(name, generateName());
        this.components = new HashMap<>();
    }

    private String generateName() {
        return UUID.randomUUID().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
    public <E extends Component> E getComponent(Class<E> clazz) {
        return (E) components.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <E extends Component> E removeComponent(Class<E> clazz) {
        if (Transform.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Transform component cannot be removed from entity!");
        }
        return (E) components.remove(clazz);
    }

}
