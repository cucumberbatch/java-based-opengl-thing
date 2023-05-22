package ecs.shapes;

import vectors.Vector2f;
import ecs.graphics.Window;


public class Rectangle implements Shape2d {

    public Vector2f topLeft;
    public Vector2f bottomRight;


    public Rectangle(Vector2f topLeft, Vector2f bottomRight) {
        this.topLeft     = topLeft;
        this.bottomRight = bottomRight;
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
