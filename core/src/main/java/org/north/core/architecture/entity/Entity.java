package org.north.core.architecture.entity;

import org.north.core.architecture.tree.v2.LinkedTreeNode;
import org.north.core.component.Transform;
import org.north.core.management.data.Identifiable;
import org.north.core.management.data.IdentifierAlreadySetException;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

/**
 * Entity is an object that contains a collection of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
// TODO: get rid of TreeNode things, an Entity must be plain object without any unnecessary hierarchy. A hierarchy can be achieved by usage of specific components in future
public class Entity
        extends LinkedTreeNode<Entity>
        implements Identifiable<UUID>, Externalizable {

    // TODO: the type of id can be just a simple int or long primitive, an int could be ok
    public UUID id;

    // TODO: move entity name into separate component, make names of limited length, i.e.: FixedString8, FixedString16, and so on. Also, make FixedString classes implement CharSequence to better Java ecosystem integration
    public String name;

    // TODO:

    // TODO: remove hardcoded reference to Transform component, Transform is not necessarily must be present for all of the entities
    public Transform transform;

    public Entity(String name) {
        UUID id = UUID.randomUUID();
        this.id = id;
        this.name = name != null ? name : id.toString();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) throws IdentifierAlreadySetException {
        if (this.id == null)
            throw new IdentifierAlreadySetException();

        this.id = id;
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

            // We make relative translation by zero to simply mark a transform component as dirty, so the next getGlobal... something will cause global cache update
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(id);
        out.writeUTF(name);
        out.writeObject(parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException {
        id = (UUID) in.readObject();
        name = in.readUTF();
        parent = (Entity) in.readObject();
    }

    @Override
    public String toString() {
        return name;
//        return "Entity{" +
//                "name='" + name + '\'' +
//                ", daughters=" + getSubtrees() +
//                '}';
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
