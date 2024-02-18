package org.north.core.components;

import org.north.core.graphics.Texture;
import org.north.core.graphics.VertexArray;
import org.north.core.shapes.Rectangle;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Button extends AbstractComponent {

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

    public VertexArray vertexBuffer;

    public Vector4f buttonDefaultColor = new Vector4f(0.75f, 0.75f, 0.75f, 1.0f);
    public Vector4f buttonOnHoverColor = new Vector4f(0.87f, 0.87f, 1.00f, 1.0f);
    public Vector4f buttonColor        = new Vector4f(buttonDefaultColor);

    public Rectangle buttonShape       = new Rectangle(new Vector2f().zero(), new Vector2f().zero());;
    public Texture   buttonTexture;

    public float transitionTimeLimit       = 1.2f;
    public float transitionTimeAccumulator = 0.0f;

    public int buttonState = 0;

    @Override
    protected void serialize(ObjectOutputStream out) throws IOException {

    }

    @Override
    protected void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {

    }
}
