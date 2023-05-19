package ecs.components;

import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.shapes.Rectangle;
import vectors.Vector2f;
import vectors.Vector4f;

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

    public Rectangle buttonShape       = new Rectangle(Vector2f.zero(), Vector2f.zero());;
    public Texture   buttonTexture;

    public float transitionTimeLimit       = 1.2f;
    public float transitionTimeAccumulator = 0.0f;

    public int buttonState = 0;
}
