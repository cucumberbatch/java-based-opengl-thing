package ecs.graphics;

public class Mesh {
    public static final Mesh BOX = new Mesh(
            new float[]{
                    -1.0f, -1.0f, 0.0f,
                    -1.0f,  1.0f, 0.0f,
                     1.0f,  1.0f, 0.0f,
                     1.0f, -1.0f, 0.0f
            },
            new byte[]{ 0, 1, 2, 2, 3, 0 },
            new float[]{
                    0,  1,
                    0,  0,
                    1,  0,
                    1,  1
            }
    );

    public float[] vertices;
    public byte[]  indices;
    public float[] uv;

    public VertexArray vertexArray;

    public Mesh() {}

    public Mesh(float[] vertices, byte[] indices, float[] uv) {
        this.vertices = vertices;
        this.indices  = indices;
        this.uv       = uv;
        this.vertexArray = new VertexArray(vertices, indices, uv);
    }
}
