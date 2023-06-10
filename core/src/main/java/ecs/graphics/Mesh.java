package ecs.graphics;

public class Mesh {
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
