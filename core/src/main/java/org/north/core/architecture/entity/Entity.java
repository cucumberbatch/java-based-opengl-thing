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
public class Entity
        extends LinkedTreeNode<Entity>
        implements Identifiable<UUID>, Externalizable {

    public UUID id;
    public String name;

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
        return transform;
    }

    @Override
    public boolean add(Entity entity) {
        if (parent != null && parent.transform != null && super.add(entity)) {
            transform.parent = parent.transform;
            return true;
        }
        return false;
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
//        out.writeObject(getTransform());
//        out.writeObject(components);
        out.writeObject(parent);
        //        out.writeObject(daughters);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException {
        id = (UUID) in.readObject();
        name = in.readUTF();
//        transform = (Transform) in.readObject();
//        components = (Map<Class<? extends Component>, Component>) in.readObject();
        parent = (Entity) in.readObject();
        //        daughters = (List<Entity>) in.readObject();
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
