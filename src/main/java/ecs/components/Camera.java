package ecs.components;

import ecs.math.Matrix4f;
import ecs.math.Vector3f;

public class Camera extends AbstractECSComponent {
    public Vector3f at      = new Vector3f();
    public Vector3f up      = new Vector3f();
    public Vector3f eye     = new Vector3f();

    public Matrix4f lookAtMatrix = new Matrix4f();
    public Matrix4f viewMatrix   = new Matrix4f();

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String toString() {
        return  "\nat:   " + at  +
                "\nup:   " + up  +
                "\neye:  " + eye +
                "\nlook: " + lookAtMatrix +
                "\nview: " + viewMatrix;
    }
}
