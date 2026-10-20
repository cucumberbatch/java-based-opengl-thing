package org.north.core.graphics;

public class Mesh {
    private static final int VERTEX_ATTRIBUTE = 0;
    private static final int TEXTURE_COORDINATE_ATTRIBUTE = 1;

    private VertexArrayHandle vao;
    private BufferHandle vertexBuffer;
    private BufferHandle textureCoordinatesBuffer;
    private BufferHandle indexBuffer;

    public final float[] vertices;
    public final byte[]  indices;
    public final float[] uv;

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
    }

    public void upload(Graphics graphics) {
        if (vao != null) return;

        VertexArrayHandle newVao = graphics.createVertexArray();
        graphics.bindVertexArray(newVao);

        BufferHandle newVertexBuffer = graphics.createArrayBuffer(vertices, VERTEX_ATTRIBUTE, 3);
        BufferHandle newTextureCoordinatesBuffer = graphics.createArrayBuffer(
                uv,
                TEXTURE_COORDINATE_ATTRIBUTE,
                2
        );
        BufferHandle newIndexBuffer = graphics.createElementBuffer(indices);

        graphics.bindVertexArray(null);

        this.vao = newVao;
        this.vertexBuffer = newVertexBuffer;
        this.textureCoordinatesBuffer = newTextureCoordinatesBuffer;
        this.indexBuffer = newIndexBuffer;
    }

    public boolean isUploaded() {
        return vao != null;
    }

    public VertexArrayHandle vao() {
        if (vao == null) {
            throw new IllegalStateException("Mesh is not uploaded to the GPU yet");
        }
        return vao;
    }

    public int indexCount() {
        return indices.length;
    }

    public void unload(Graphics graphics) {
        if (vao == null) return;

        graphics.deleteBuffer(vertexBuffer);
        graphics.deleteBuffer(textureCoordinatesBuffer);
        graphics.deleteBuffer(indexBuffer);
        graphics.deleteVertexArray(vao);

        vao = null;
        vertexBuffer = null;
        textureCoordinatesBuffer = null;
        indexBuffer = null;
    }
}
