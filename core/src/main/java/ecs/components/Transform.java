package ecs.components;

import ecs.physics.collision.TransformListener;
import vectors.Vector3f;

/**
 * The main component of each game object that tells
 * you about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends AbstractComponent {
    private TransformListener transformListener = (e, p1, p2) -> {};

    public Transform parent;

    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();
    public Vector3f scale    = Vector3f.one();


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

    @Override
    public void reset() {
        super.reset();
        Vector3f.reset(
                position,
                rotation,
                scale
        );
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
}
