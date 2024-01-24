package org.north.core.entities;

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
public class Entity implements TreeNode<Entity>, Collidable {
    public long     id;
    public String   name;
    public Entity   parent;
    public List<Entity> daughters;

    public Transform transform;
    public Map<Class<? extends Component>, Component> components;

    public Entity() {
        this(UUID.randomUUID().toString());
    }

    public Entity(String name) {
        this.id = -1;
        this.name = name;
        this.daughters = new LinkedList<>();
        this.components = new HashMap<>();
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
        return (E) components.remove(clazz);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Entity getParent() {
        return parent;
    }

    @Override
    public List<Entity> getDaughters() {
        return daughters;
    }

    @Override
    public List<Entity> getSiblings() {
        LinkedList<Entity> siblings = new LinkedList<>();

        if (this.parent != null) {
            for (Entity sibling : this.parent.daughters) {
                if (sibling != this) {
                    siblings.add(sibling);
                }
            }
        }

        return siblings;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setParent(Entity parent) {
        if (this.parent != null) {
            this.parent.daughters.remove(this);
        }
        if (this.daughters.contains(parent)) {
            this.daughters.remove(parent);
            parent.parent = null;
        }
        if (this.parent != parent) {
            parent.daughters.add(this);
        }
        this.parent = parent;
    }

    @Override
    public void setDaughters(List<Entity> daughters) {
        this.daughters = daughters;
    }

    @Override
    public boolean isParentOf(Entity entity) {
        return this.daughters.contains(entity);
    }
}
