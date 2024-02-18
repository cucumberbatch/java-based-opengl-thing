package org.north.core.components;

import org.north.core.graphics.Mesh;
import org.north.core.graphics.Texture;
import org.joml.Vector4f;
import org.north.core.graphics.shader.Shader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GasCloud extends AbstractComponent {

    public Mesh mesh;
    public Shader shader;
    public Texture texture;
    public Vector4f color = new Vector4f();

    @Override
    protected void serialize(ObjectOutputStream out) throws IOException {

    }

    @Override
    protected void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {

    }
}
