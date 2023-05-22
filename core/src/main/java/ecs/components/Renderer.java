package ecs.components;

import ecs.graphics.Texture;
import ecs.graphics.VertexArray;

public class Renderer extends AbstractComponent {

//    public float[] vertices = new float[]{
//            -0.5f, -0.5f, 0.0f,
//            -0.5f,  0.5f, 0.0f,
//             0.5f,  0.5f, 0.0f,
//             0.5f, -0.5f, 0.0f
//    };

    /*
    public float[] vertices = new float[]{
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f, -0.5f,  0.5f
    };

    public byte[] indices = new byte[]{
            0, 1, 2, 2, 3, 0,
            0, 4, 5, 5, 1, 0,
            7, 6, 2, 2, 3, 7,
            5, 1, 2, 2, 6, 5,
            4, 0, 3, 3, 7, 4,
            4, 5, 6, 6, 7, 4
    };

    public float[] uv = new float[]{
            0,  1,
            0,  0,
            1,  0,
            1,  1,
            0,  1,
            0,  0,
            1,  0,
            1,  1,
            0,  1,
            0,  0,
            1,  0,
            1,  1,
            0,  1,
            0,  0,
            1,  0,
            1,  1,
            0,  1,
            0,  0,
            1,  0,
            1,  1,
            0,  1,
            0,  0,
            1,  0,
            1,  1
    };
    */

    public float[] vertices = new float[]{
            -1.0f, -1.0f, +0.0f,
            -1.0f, +1.0f, +0.0f,
            +1.0f, +1.0f, +0.0f,
            +1.0f, -1.0f, +0.0f
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
    public Texture     texture;


    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String toString() {
        return "renderer component";
    }
}
