package org.north.core.component;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.north.core.architecture.entity.Entity;
import org.north.core.physics.collision.TransformListener;
import org.joml.Vector3f;

import java.io.*;

/**
 * The main component of each game object that tells
 * you about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends AbstractComponent implements Cloneable {
    public Transform parent;

    public Vector3f position = new Vector3f(0, 0, 0);
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public Vector3f scale    = new Vector3f(1, 1, 1);

    private static final TransformListener EMPTY_TRANSFORM_LISTENER = (e, p1, p2) -> {};

    private TransformListener transformListener = EMPTY_TRANSFORM_LISTENER;


    public void moveTo(Vector3f position) {
        if (EMPTY_TRANSFORM_LISTENER != transformListener) {
            transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(position));
        }
        this.position.set(position);
    }

    public void moveTo(float x, float y, float z) {
        if (EMPTY_TRANSFORM_LISTENER != transformListener) {
            transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(x, y, z));
        }
        this.position.set(x, y, z);
    }

    public void moveRel(Vector3f position) {
        if (EMPTY_TRANSFORM_LISTENER != transformListener) {
            transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(position).add(this.position));
        }
        this.position.add(position);
    }

    public void moveRel(float x, float y, float z) {
        if (EMPTY_TRANSFORM_LISTENER != transformListener) {
            transformListener.registerMovement(this.entity, new Vector3f(this.position), new Vector3f(x, y, z).add(this.position));
        }
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
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .rotateZ(rotation.z)
                .scale(scale.x, scale.y, scale.z);
    }

    public Matrix4f getGlobalModelMatrix(Matrix4f destination) {
        Transform it;
        Entity parentEntity = this.entity;
        destination.identity();
        do {
            it = parentEntity.transform;
            destination
                    .translate(it.position)
                    .rotateX(it.rotation.x)
                    .rotateY(it.rotation.y)
                    .rotateZ(it.rotation.z)
                    .scale(it.scale);
            parentEntity = it.entity.getParent();
        } while (parentEntity != null);
        return destination;
    }

    public Vector3f getGlobalPosition(Vector3f destination) {
        return getGlobalModelMatrix(new Matrix4f()).getTranslation(destination);
    }

    public Vector3f getGlobalRotation(Vector3f destination) {
        Transform it;
        Entity parentEntity = this.entity;
        destination.zero();
        do {
            it = parentEntity.transform;
            destination
                    .rotateX(it.rotation.x)
                    .rotateY(it.rotation.y)
                    .rotateZ(it.rotation.z);
            parentEntity = it.entity.getParent();
        } while (parentEntity != null);
        return destination;
    }

    public Vector3f getGlobalScale(Vector3f destination) {
        return getGlobalModelMatrix(new Matrix4f()).getScale(destination);
    }

    public Transform getGlobalTransform() {
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
            iterableTransform = iterableTransform.parent;
        } while (iterableTransform != null);
        return worldTransform;
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
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(position);
        out.writeObject(rotation);
        out.writeObject(scale);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
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
