package org.north.core.systems;

import org.north.core.graphics.Texture;
import org.north.core.graphics.VertexArray;

public class CubeMesh {

    public float[] vertices = new float[]{
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
             0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
             0.5f,  0.5f, -0.5f, 1.0f, 1.0f,
             0.5f,  0.5f, -0.5f, 1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
             0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
             0.5f,  0.5f, 0.5f, 1.0f, 1.0f,
             0.5f,  0.5f, 0.5f, 1.0f, 1.0f,
            -0.5f,  0.5f, 0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f, 1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f, 0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f, 1.0f, 0.0f,

             0.5f,  0.5f,  0.5f, 1.0f, 0.0f,
             0.5f,  0.5f, -0.5f, 1.0f, 1.0f,
             0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
             0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
             0.5f, -0.5f,  0.5f, 0.0f, 0.0f,
             0.5f,  0.5f,  0.5f, 1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
             0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
             0.5f, -0.5f,  0.5f, 1.0f, 0.0f,
             0.5f, -0.5f,  0.5f, 1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
             0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
             0.5f, 0.5f,  0.5f, 1.0f, 0.0f,
             0.5f, 0.5f,  0.5f, 1.0f, 0.0f,
            -0.5f, 0.5f,  0.5f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
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
