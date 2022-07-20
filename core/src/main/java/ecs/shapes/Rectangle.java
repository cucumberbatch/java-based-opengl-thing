package ecs.shapes;

import vectors.Vector2f;
import ecs.gl.Window;


public class Rectangle implements ConvertibleShape {

    public Vector2f topLeft     = null;
    public Vector2f bottomRight = null;


    public Rectangle(Vector2f topLeft, Vector2f bottomRight) {
        this.topLeft     = topLeft;
        this.bottomRight = bottomRight;
    }

    public boolean isInside(Vector2f point) {
        return  (point.x >= this.topLeft.x && point.x <= this.bottomRight.x) && 
                (point.y >= this.topLeft.y && point.y <= this.bottomRight.y);
    }

    public boolean isIntersects(Rectangle rectButton) {
        return !(rectButton.bottomRight.x < this.topLeft.x ||
                 rectButton.bottomRight.y < this.topLeft.y ||
                 rectButton.topLeft.x > this.bottomRight.x ||
                 rectButton.topLeft.y > this.bottomRight.y);
    }

    public boolean isIdentical(Rectangle rectButton) {
        float epsilon = 0.01f;

        float thisWidth  = this.bottomRight.x - this.topLeft.x;
        float thisHeight = this.bottomRight.y - this.topLeft.y;
        float rectWidth  = rectButton.bottomRight.x - rectButton.topLeft.x;
        float rectHeight = rectButton.bottomRight.y - rectButton.topLeft.y;

        return Math.abs(rectWidth  - thisWidth)  < epsilon &&
               Math.abs(rectHeight - thisHeight) < epsilon;
    }

    @Override
    public float[] convertToVertices() {
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
