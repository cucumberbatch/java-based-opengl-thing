package org.north.core.components;

import org.joml.Matrix4f;
import org.north.core.physics.collision.TransformListener;
import org.joml.Vector3f;

/**
 * The main component of each game object that tells
 * you about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends AbstractComponent implements Cloneable {
    private TransformListener transformListener = (e, p1, p2) -> {};

    public Transform parent;

    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();
    public Vector3f scale    = new Vector3f(1, 1, 1);


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

    public Matrix4f getLocalModelMatrix() {
        return new Matrix4f().identity()
                .translate(position.x, position.y, position.z)
                .scale(scale.x, scale.y, scale.z);
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public String toString() {
        return  "\nposition: " + position +
                "\nrotation: " + rotation +
                "\nscale:    " + scale;
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
