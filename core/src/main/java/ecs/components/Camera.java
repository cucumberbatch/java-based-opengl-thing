package ecs.components;

import matrices.Matrix4f;
import vectors.Vector3f;

import ecs.utils.TerminalUtils;

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
        return  /*String.format(
            " v \n%s\n%s\n%s\n%s\n%s\n",
            at, up, eye,
            TerminalUtils.formatOutputMatrix(lookAtMatrix),
            TerminalUtils.formatOutputMatrix(viewMatrix)
        );*/
        
                "\nat:   " + at  +
                "\nup:   " + up  +
                "\neye:  " + eye +
                "\nlook: " + lookAtMatrix +
                "\nview: " + viewMatrix;
        
    }
}
