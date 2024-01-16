package org.north.core.shapes;

import org.joml.Vector2f;
import org.north.core.graphics.Window;


public class Rectangle implements Shape2d {

    public Vector2f topLeft;

    // todo: i think, more convenient usage would be using height and width
    //  instead of using bottom right corner point
    public Vector2f bottomRight;


    public Rectangle(Vector2f topLeft, Vector2f bottomRight) {
        this.topLeft     = topLeft;
        this.bottomRight = bottomRight;
    }

    public void moveTo(Vector2f position) {
        float width = Math.abs(this.bottomRight.x - this.topLeft.x);
        float height = Math.abs(this.topLeft.y - this.bottomRight.y);
        this.topLeft.x = position.x - width / 2;
        this.bottomRight.x = position.x + width / 2;
        this.bottomRight.y = position.y - height / 2;
        this.topLeft.y = position.y + height / 2;
    }

    public boolean isInside(Vector2f point) {
        return  (point.x >= this.topLeft.x && point.x <= this.bottomRight.x) &&
                (point.y >= this.topLeft.y && point.y <= this.bottomRight.y);
    }

    public boolean isInside(Rectangle rectangle) {
        return  isInside(rectangle.topLeft) &&
                isInside(rectangle.bottomRight);
    }

    public boolean isIntersects(Rectangle rectangle) {
        return !(rectangle.bottomRight.x < this.topLeft.x ||
                 rectangle.bottomRight.y < this.topLeft.y ||
                 rectangle.topLeft.x > this.bottomRight.x ||
                 rectangle.topLeft.y > this.bottomRight.y);
    }

    public boolean isIdentical(Rectangle rectangle) {
        float epsilon = 0.01f;

        float thisWidth  = this.bottomRight.x - this.topLeft.x;
        float thisHeight = this.bottomRight.y - this.topLeft.y;
        float rectWidth  = rectangle.bottomRight.x - rectangle.topLeft.x;
        float rectHeight = rectangle.bottomRight.y - rectangle.topLeft.y;

        return Math.abs(rectWidth  - thisWidth)  < epsilon &&
               Math.abs(rectHeight - thisHeight) < epsilon;
    }

    @Override
    public float[] toVertices() {
        float[] vertices = new float[12];

        Vector2f screenSpaceTopLeft     = Window.translatePointToWindow(topLeft);
        Vector2f screenSpaceBottomRight = Window.translatePointToWindow(bottomRight);

        vertices[0]  = screenSpaceTopLeft.x;
        vertices[1]  = screenSpaceTopLeft.y;

        vertices[3]  = screenSpaceTopLeft.x;
        vertices[4]  = screenSpaceBottomRight.y;

        vertices[6]  = screenSpaceBottomRight.x;
        vertices[7]  = screenSpaceBottomRight.y;

        vertices[9]  = screenSpaceBottomRight.x;
        vertices[10] = screenSpaceTopLeft.y;

        return vertices;
    }

}
