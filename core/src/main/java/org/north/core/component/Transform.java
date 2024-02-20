package org.north.core.component;

import org.joml.Matrix4f;
import org.north.core.architecture.entity.Entity;
import org.north.core.physics.collision.TransformListener;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The main component of each game object that tells
 * you about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends AbstractComponent implements Cloneable {
    private transient TransformListener transformListener = (e, p1, p2) -> {};

    public transient Transform parent;

    public transient Vector3f position = new Vector3f();
    public transient Vector3f rotation = new Vector3f();
    public transient Vector3f scale    = new Vector3f(1, 1, 1);


    public void moveTo(Vector3f position) {
        transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(position));
        this.position.set(position);
    }

    public void moveTo(float x, float y, float z) {
        transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(x, y, z));
        this.position.set(x, y, z);
    }

    public void moveRel(Vector3f position) {
        transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(position).add(this.position));
        this.position.add(position);
    }

    public void moveRel(float x, float y, float z) {
        transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(x, y, z).add(this.position));
        this.position.add(x, y, z);
    }

    public void setTransformListener(TransformListener transformListener) {
        this.transformListener = transformListener;
    }

    /**
     * Returns a local model matrix of this transform
     *
     * @apiNote memory consumption! matrix object creation on each call
     * @deprecated use method with matrix argument as temporary destination object
     * @return a local model matrix of this transform
     */
    public Matrix4f getLocalModelMatrix() {
        return getLocalModelMatrix(new Matrix4f());
    }

    public Matrix4f getLocalModelMatrix(Matrix4f destination) {
        return destination.identity()
                .translate(position.x, position.y, position.z)
                .scale(scale.x, scale.y, scale.z);
    }

    public Matrix4f getWorldModelMatrix(Matrix4f destination) {
        Transform iterableTransform = this;
        Transform worldTransform = new Transform();
        do {
            worldTransform.position.add(iterableTransform.position);
            worldTransform.rotation.add(iterableTransform.rotation);
            worldTransform.scale.set(
                    worldTransform.scale.x * iterableTransform.scale.x,
                    worldTransform.scale.y * iterableTransform.scale.y,
                    worldTransform.scale.z * iterableTransform.scale.z
            );
            Entity entityParent = iterableTransform.entity.getParent();
            iterableTransform = (entityParent != null) ? entityParent.get(Transform.class) : null;
        } while (iterableTransform != null);

        destination.identity()
                .translate(worldTransform.position.x, worldTransform.position.y, worldTransform.position.z)
                .scale(worldTransform.scale.x, worldTransform.scale.y, worldTransform.scale.z);

        return destination;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public String toString() {
        return  "\nposition: " + position +
                "\nrotation: " + rotation +
                "\nscale:    " + scale +
                super.toString();
    }

    @Override
    public void serializeObject(ObjectOutputStream out) throws IOException {
        super.serializeObject(out);
        out.writeObject(position);
        out.writeObject(rotation);
        out.writeObject(scale);
    }

    @Override
    public void deserializeObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.deserializeObject(in);
        position = (Vector3f) in.readObject();
        rotation = (Vector3f) in.readObject();
        scale = (Vector3f) in.readObject();
    }

    @Override
    public Transform clone() {
        Transform clone = (Transform) super.clone();
        clone.parent = this.parent;
        clone.position = new Vector3f(this.position);
        clone.rotation = new Vector3f(this.rotation);
        clone.scale = new Vector3f(this.scale);
        return clone;
    }
}
