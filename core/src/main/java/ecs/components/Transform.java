package ecs.components;

import vectors.Vector3f;

/**
 * The main component of each game object that tells
 * you about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends AbstractComponent {
    public Transform parent;

    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();
    public Vector3f scale    = Vector3f.one();


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
    public Transform getInstance() {
        return new Transform();
    }

    @Override
    public Transform getReplica() {
        Transform transform = new Transform();
        transform.position = new Vector3f(position);
        transform.rotation = new Vector3f(rotation);
        transform.scale = new Vector3f(scale);
        return transform;
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
