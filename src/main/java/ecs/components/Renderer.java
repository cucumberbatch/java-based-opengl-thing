package ecs.components;

import ecs.graphics.Texture;
import ecs.graphics.VertexArray;

public class Renderer extends AbstractComponent {

    public float[] vertices = new float[]{
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f, -10.0f * 9.0f / 16.0f, 0.0f
    };

    public byte[] indices = new byte[]{
            0, 1, 2,
            2, 3, 0
    };

    public float[] uv = new float[]{
            0, 1,
            0, 0,
            1, 0,
            1, 1
    };

    public VertexArray background;
    public Texture texture;

}
