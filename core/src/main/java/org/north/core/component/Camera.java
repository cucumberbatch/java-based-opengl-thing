package org.north.core.component;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends AbstractComponent {
    public Vector3f at      = new Vector3f();
    public Vector3f up      = new Vector3f();
    public Vector3f eye     = new Vector3f();

    public float near = 0.1f;
    public float far = 100f;
    public float angle = (float) Math.toRadians(90f);
    public float ratio = 1f / 1f;

    public Matrix4f viewMatrix       = new Matrix4f();
    public Matrix4f projectionMatrix = new Matrix4f();

    public Vector3f getPosition() {
        return new Vector3f(this.getTransform().position);
    }

    public Vector3f getPosition(Vector3f destination) {
        return destination.set(this.getTransform().position);
    }

    @Override
    public String toString() {
        return "\nat:   " + at +
                "\nup:   " + up +
                "\neye:  " + eye +
                "\nview: " + viewMatrix +
                "\nprojection: " + projectionMatrix;

    }

}
