package ecs.entities;

import ecs.components.Component;
import ecs.components.Transform;
import ecs.physics.Collidable;

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
    public List<Entity> daughters = new LinkedList<>();

    public Transform transform;
    public Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity() {
        this.id = -1;
        this.name = UUID.randomUUID().toString();
    }

    public Entity(String name) {
        this.id = -1;
        this.name = name;
    }

    public void addComponent(Component component) {
        components.put(component.getClass(), component);
        component.setEntity(this);
    }

    public <E extends Component> E getComponent(Class<E> clazz) {
        return (E) components.get(clazz);
    }

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
        if (this.parent == null) {
            return new LinkedList<>();
        }

        LinkedList<Entity> siblings = new LinkedList<>(this.parent.daughters);
        siblings.removeIf(this::equals);

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
        this.daughters = (List<Entity>) daughters;
    }

    @Override
    public boolean isParentOf(Entity entity) {
        return this.daughters.contains(entity);
    }
}
