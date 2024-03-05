package org.north.core.utils;

import org.north.core.component.MeshRenderer;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Mesh;
import org.north.core.graphics.shader.AbstractGLShader;
import org.north.core.graphics.shader.Shader;

import java.io.*;
import java.util.UUID;

public class SerializationUtils {
    public static UUID readUUID(ObjectInput in) throws IOException {
        return new UUID(in.readLong(), in.readLong());
    }

    public static void writeUUID(ObjectOutput out, UUID uuid) throws IOException {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    public static Mesh readMesh(ObjectInput in) throws IOException, ClassNotFoundException {
        float[] vertices = (float[]) in.readObject();
        byte[] indices = (byte[]) in.readObject();
        float[] uv = (float[]) in.readObject();
        return new Mesh(vertices, indices, uv);
    }

    public static void writeMesh(ObjectOutput out, Mesh mesh) throws IOException {
        out.writeObject(mesh.vertices);
        out.writeObject(mesh.indices);
        out.writeObject(mesh.uv);
    }

    public static Shader readShader(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static void writeShader(ObjectOutput out, Shader shader) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
