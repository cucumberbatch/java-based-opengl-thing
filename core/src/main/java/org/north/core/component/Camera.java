package org.north.core.component;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(at);
        out.writeObject(up);
        out.writeObject(eye);
        out.writeFloat(near);
        out.writeFloat(far);
        out.writeFloat(angle);
        out.writeFloat(ratio);
        out.writeObject(viewMatrix);
        out.writeObject(projectionMatrix);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        at = (Vector3f) in.readObject();
        up = (Vector3f) in.readObject();
        eye = (Vector3f) in.readObject();
        near = in.readFloat();
        far = in.readFloat();
        angle = in.readFloat();
        ratio = in.readFloat();
        viewMatrix = (Matrix4f) in.readObject();
        projectionMatrix = (Matrix4f) in.readObject();
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
