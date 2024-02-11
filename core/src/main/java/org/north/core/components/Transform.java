package org.north.core.components;

import org.joml.Matrix4f;
import org.north.core.physics.collision.TransformListener;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The main component of each game object that tells
 * you about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends AbstractComponent implements Serializable, Cloneable {
    private transient TransformListener transformListener = (e, p1, p2) -> {};

    private transient static final long serialVersionUID = 1L;

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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.write(42);
//        out.writeObject(position);
//        out.writeObject(rotation);
//        out.writeObject(scale);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        this.position = (Vector3f) in.readObject();
//        this.rotation = (Vector3f) in.readObject();
//        this.scale = (Vector3f) in.readObject();

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
    public String getName() {
        return this.getClass().getName();
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
