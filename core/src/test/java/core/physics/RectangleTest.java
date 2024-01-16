package core.physics;

import org.north.core.shapes.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.joml.Vector2f;

class RectangleTest {

    private static final Vector2f point1  = new Vector2f(0.0f, 0.0f);
    private static final Vector2f point2  = new Vector2f(2.0f, 0.0f);
    private static final Vector2f point3  = new Vector2f(0.0f, 2.0f);
    private static final Vector2f point4  = new Vector2f(2.0f, 2.0f);

    private static final Vector2f point5  = new Vector2f(1.0f, 1.0f);
    private static final Vector2f point6  = new Vector2f(3.0f, 1.0f);
    private static final Vector2f point7  = new Vector2f(1.0f, 3.0f);
    private static final Vector2f point8  = new Vector2f(3.0f, 3.0f);

    private static final Vector2f point9  = new Vector2f(5.0f, 2.0f);
//    private static final Vector2f point10 = new Vector2f(3.0f, 1.0f);
//    private static final Vector2f point11 = new Vector2f(1.0f, 3.0f);
//    private static final Vector2f point12 = new Vector2f(3.0f, 3.0f);


    @Test
    void isInsideTestInside1() {
        Rectangle rect1 = new Rectangle(point1, point8);
        Vector2f insidePoint1 = point5;

        Assertions.assertTrue(rect1.isInside(insidePoint1));
    }

    @Test
    void isInsideTestOutside2() {
        Rectangle rect1 = new Rectangle(point4, point8);
        Vector2f outsidePoint1 = point1;

        Assertions.assertFalse(rect1.isInside(outsidePoint1));
    }

    @Test
    void isIntersectsTestCornerIntersection1() {
        Rectangle rect1 = new Rectangle(point1, point4);
        Rectangle rect2 = new Rectangle(point5, point8);

        Assertions.assertTrue(rect1.isIntersects(rect2));
    }

    @Test
    void isIntersectsTestNotIntersects2() {
        Rectangle rect1 = new Rectangle(point1, point5);
        Rectangle rect2 = new Rectangle(point4, point8);

        Assertions.assertFalse(rect1.isIntersects(rect2));
    }

    @Test
    void isIntersectsTestCornerIntersection3() {
        Rectangle rect1 = new Rectangle(point1, point9);
        Rectangle rect2 = new Rectangle(point2, point8);

        Assertions.assertTrue(rect1.isIntersects(rect2));
    }

    @Test
    void isIntersectsTestNotIntersects4() {
        Rectangle rect1 = new Rectangle(point1, point6);
        Rectangle rect2 = new Rectangle(point3, point8);

        Assertions.assertFalse(rect1.isIntersects(rect2));
    }

}