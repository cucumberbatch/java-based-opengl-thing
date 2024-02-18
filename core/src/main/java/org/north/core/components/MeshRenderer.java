package org.north.core.components;

import org.north.core.graphics.*;
import org.joml.Vector4f;
import org.north.core.graphics.shader.Shader;
import org.north.core.graphics.shader.SimpleColorShader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.lwjgl.opengl.GL11.*;

public class MeshRenderer extends AbstractComponent {
    public Mesh     mesh = PredefinedMeshes.QUAD;
    public Shader shader = new SimpleColorShader();
    public Texture  texture = null;
    public Texture  texture2 = null;
    public Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
    public int renderType = GL_TRIANGLES;

    @Override
    protected void serialize(ObjectOutputStream out) throws IOException {

    }

    @Override
    protected void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {

    }
}
