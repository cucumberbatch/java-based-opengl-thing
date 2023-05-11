package ecs.components;

import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import vectors.Vector2f;
import ecs.shapes.Rectangle;


public class PlaneRenderer extends AbstractComponent {

    public Vector2f initialTopLeftVertex     = new Vector2f(380.f, 320.f);
    public Vector2f initialBottomRightVertex = new Vector2f(440.f, 480.f);

    public Rectangle idleCursorForm = new Rectangle(new Vector2f(0.f, 0.f), new Vector2f(10.f, 10.f));


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
    public Texture     texture;
    public Rectangle   cursor;
    public Rectangle   button;
    public VertexArray buttonBackground;
    public Texture     buttonTexture;

}
