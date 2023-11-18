package org.north.core.components;

import org.north.core.graphics.*;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class MeshRenderer extends AbstractComponent {
    public Mesh     mesh = new Mesh();
    public Shader   shader = new SimpleColorShader();
    public Texture  texture = null;
    public Texture  texture2 = null;
    public Vector4f color = new Vector4f(0.5f, 0.6f, 0.7f, 1);
    public int renderType = GL_TRIANGLES;
}
