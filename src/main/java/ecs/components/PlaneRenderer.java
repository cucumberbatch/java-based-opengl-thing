package ecs.components;

import ecs.graphics.Texture;
import ecs.graphics.VertexArray;

public class PlaneRenderer extends AbstractECSComponent {

    public float[] vertices = new float[]{
            -0.5f, -0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
             0.5f,  0.5f, 0.0f,
             0.5f, -0.5f, 0.0f
    };

    public byte[] indices = new byte[]{
            0, 1, 2, 2, 3, 0,
    };

    public float[] uv = new float[]{
            0,  1,
            0,  0,
            1,  0,
            1,  1
    };

    public VertexArray background;
    public Texture texture;


    @Override
    public String toString() {
        return null;
    }
}
