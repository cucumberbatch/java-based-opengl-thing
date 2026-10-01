package org.north.core.scene;

import org.north.core.scene.component.management.ComponentManager;
import org.north.core.scene.tree.LinkedTreeNode;
import org.north.core.scene.component.Transform;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Entity is an object that contains a collection of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
// TODO: get rid of TreeNode things, an Entity must be plain object without any unnecessary hierarchy. A hierarchy can be achieved by usage of specific components in future
public class Entity
        extends LinkedTreeNode<Entity> {

    // TODO: move idSequence to the EntityManager or something and add a specific constructor with id
    private static final AtomicInteger idSequence = new AtomicInteger(1);

    private final ComponentManager componentManager;

    public int id;

    // TODO: move entity name into separate component, make names of limited length, i.e.: FixedString8, FixedString16, and so on. Also, make FixedString classes implement CharSequence to better Java ecosystem integration
    public String name;

    public Entity(String name, ComponentManager componentManager) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Entity name must be present as a non blank string in a constructor argument");

        if (componentManager == null)
            throw new NullPointerException("A provided component manager must not be null");

        this.id   = idSequence.getAndIncrement();
        this.name = name;
        this.componentManager = componentManager;
    }

    public String getName() {
        return name;
    }

    public Entity getByName(String name) {
        return super.findFirst(node -> node.getName().equals(name));
    }

    public Transform getTransform() {
        return componentManager.get(this, Transform.class);
    }

    public void setTransform(Transform transform) {
        throw new UnsupportedOperationException("Currently it's not supported to modify transform of entity using this method");
    }

    @Override
    public boolean add(Entity entity) {
        return super.add(entity);
    }

    @Override
    public boolean remove(Entity entity) {
        return super.remove(entity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity other = (Entity) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Entity{id=" + id + "}";
    }
}
