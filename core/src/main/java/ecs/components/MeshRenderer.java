package ecs.components;

import ecs.graphics.Mesh;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class MeshRenderer extends AbstractComponent {
    public Mesh     mesh = new Mesh();
    public Shader   shader = Shader.GUI;
    public Texture  texture = null;
    public Vector4f color = new Vector4f(0.5f, 0.6f, 0.7f, 1);
    public int renderType = GL_LINE_LOOP;
}
