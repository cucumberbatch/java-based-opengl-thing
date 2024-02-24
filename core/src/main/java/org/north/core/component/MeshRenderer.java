package org.north.core.component;

import org.north.core.graphics.*;
import org.joml.Vector4f;
import org.north.core.graphics.shader.Shader;
import org.north.core.graphics.shader.SimpleColorShader;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;
import static org.north.core.utils.SerializationUtils.readMesh;
import static org.north.core.utils.SerializationUtils.writeMesh;

public class MeshRenderer extends AbstractComponent {
    public Mesh     mesh = PredefinedMeshes.QUAD;
    public Shader shader = new SimpleColorShader();
    public Texture  texture = null;
    public Texture  texture2 = null;
    public Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
    public int renderType = GL_TRIANGLES;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        writeMesh(out, mesh);
        out.writeObject(shader);
        out.writeObject(texture);
        out.writeObject(texture2);
        out.writeObject(color);
        out.writeInt(renderType);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        mesh = readMesh(in);
        shader = (Shader) in.readObject();
        texture = (Texture) in.readObject();
        texture2 = (Texture) in.readObject();
        color = (Vector4f) in.readObject();
        renderType = in.readInt();
    }
}
