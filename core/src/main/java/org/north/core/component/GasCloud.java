package org.north.core.component;

import org.north.core.graphics.Mesh;
import org.north.core.graphics.Texture;
import org.joml.Vector4f;
import org.north.core.graphics.shader.Shader;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class GasCloud extends AbstractComponent {

    public Mesh mesh;
    public Shader shader;
    public Texture texture;
    public Vector4f color = new Vector4f();
    public float acc = 0;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(mesh);
        out.writeObject(shader);
        out.writeObject(texture);
        out.writeObject(color);
        out.writeFloat(acc);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        mesh = (Mesh) in.readObject();
        shader = (Shader) in.readObject();
        texture = (Texture) in.readObject();
        color = (Vector4f) in.readObject();
        acc = in.readFloat();
    }
}
