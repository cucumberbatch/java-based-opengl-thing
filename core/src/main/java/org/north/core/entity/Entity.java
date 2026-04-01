package org.north.core.entity;

import org.north.core.entity.tree.LinkedTreeNode;
import org.north.core.component.Transform;

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

    public int id;

    // TODO: move entity name into separate component, make names of limited length, i.e.: FixedString8, FixedString16, and so on. Also, make FixedString classes implement CharSequence to better Java ecosystem integration
    public String name;

    // TODO: remove hardcoded reference to Transform component, Transform is not necessarily must be present for all of the entities
    public Transform transform;

    public Entity(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Entity name must be present as a non blank string in a constructor argument");

        this.id   = idSequence.getAndIncrement();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Entity getByName(String name) {
        return super.findFirst(node -> node.getName().equals(name));
    }

    public Transform getTransform() {
        return this.transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    @Override
    public boolean add(Entity entity) {
        Transform entityTransform = entity.getTransform();
        if (entityTransform != null) {
            entityTransform.parent = this.transform;

            // We make relative translation by zero to simply mark a transform component as dirty, so the next getGlobal... method call will cause global cache update
            entityTransform.moveRel(0, 0, 0);
        }
        return super.add(entity);
    }

    @Override
    public boolean remove(Entity entity) {
        if (super.remove(entity)) {
            transform.parent = null;
            return true;
        }
        return false;
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
        return "Entity{id='" + id + '}';
    }
}
