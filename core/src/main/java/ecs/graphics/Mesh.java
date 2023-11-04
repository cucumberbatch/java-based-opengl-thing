package ecs.graphics;

public class Mesh {
    public float[] vertices;
    public byte[]  indices;
    public float[] uv;

    public VertexArray vertexArray;

    public Mesh() {
        this(new float[]{
                -1.0f, -1.0f, +0.0f,
                -1.0f, +1.0f, +0.0f,
                +1.0f, +1.0f, +0.0f,
                +1.0f, -1.0f, +0.0f
        }, new byte[]{
                0, 1, 2, 2, 3, 0,
        }, new float[]{
                0,  1,
                0,  0,
                1,  0,
                1,  1
        });
    }

    public Mesh(float[] vertices, byte[] indices, float[] uv) {
        this.vertices = vertices;
        this.indices  = indices;
        this.uv       = uv;
        this.vertexArray = new VertexArray(vertices, indices, uv);
    }
}
