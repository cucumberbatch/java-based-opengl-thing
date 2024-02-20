package org.north.core.component;

import org.north.core.graphics.Texture;
import org.north.core.graphics.VertexArray;
import org.joml.Vector2f;
import org.north.core.shape.Rectangle;

public class VisualCursor extends AbstractComponent {

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

    //todo: a vertex buffer must be in a Mesh object of a MeshRenderer component
    public VertexArray vertexBuffer;

    //todo: also a texture object must move into a MeshRenderer component
    public Texture     texture;

    // A visual cursor, which can addict or "magnetize" to an object, that collides with it
    public Rectangle   cursor;

    public VertexArray background;
//    public VertexArray buttonBackground;

    public Rectangle previouslySelectedButtonShape;

    // Switching of this parameter is controlled by onCollision methods of VisualCursorSystem
    public boolean   isIntersects = false;

}
