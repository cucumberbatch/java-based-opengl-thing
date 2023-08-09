package vectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class Vector3fTest {

    @Test
    void testRotationVector1() {
        Vector3f origin = Vector3f.forward();
        Vector3f up     = Vector3f.up();

        Vector3f result = new Vector3f(origin).rotation(-90, 0, 0);
        assertEquals(up, result);
    }

    @Test
    void testRotationVector2() {
        Vector3f origin = Vector3f.forward();
        Vector3f right  = Vector3f.right();

        Vector3f result = new Vector3f(origin).rotation(0, 90, 0);
        assertEquals(right, result);
    }

    @Test
    void testRotationVector3() {
        Vector3f origin   = Vector3f.forward();
        Vector3f backward = Vector3f.backward();

        Vector3f result = new Vector3f(origin).rotation(0, 180, 0);
        assertEquals(backward, result);
    }

    @Test
    void testRotationVector4() {
        Vector3f origin   = Vector3f.forward();
        Vector3f backward = Vector3f.backward();

        Vector3f result = new Vector3f(origin)
                .rotation(90, 0, 0)
                .rotation(0, 0, 180)
                .rotation(-90, 0, 0);

        assertEquals(backward, result);
    }

    @Test
    void testRotationVector5() {
        Vector3f origin   = Vector3f.forward();
        Vector3f backward = Vector3f.backward();

        Vector3f result = new Vector3f(origin)
                .rotation(90, 0, 0)
                .rotation(0, 0, 90)
                .rotation(0, 90, 0);

        assertEquals(backward, result);
    }

    @Test
    void testCrossVector1() {
        Vector3f right   = new Vector3f(1f, 0f, 0f);
        Vector3f up      = new Vector3f(0f, 1f, 0f);
        Vector3f forward = new Vector3f(0f, 0f, 1f);

        Vector3f crossProduct = Vector3f.cross(up, forward);
        assertEquals(right, crossProduct);
    }

    @Test
    void testCrossVector2() {
        Vector3f v1 = new Vector3f(-3f, 0f, -2f);
        Vector3f v2 = new Vector3f(5f, -1f, 2f);
        Vector3f v3 = new Vector3f(-2f, -4f, 3f);

        Vector3f crossProduct = Vector3f.cross(v1, v2);
        assertEquals(v3, crossProduct);
    }

    @Test
    void testCrossVector3() {
        Vector3f v1 = new Vector3f(0f, 0f, 0f);
        Vector3f v2 = new Vector3f(1f, 1f, 1f);
        Vector3f v3 = new Vector3f(0f, 0f, 0f);

        Vector3f crossProduct = Vector3f.cross(v1, v2);
        assertEquals(v3, crossProduct);
        assertNotSame(v1, crossProduct);
    }

    @Test
    void testCrossVector4() {
        Vector3f v1 = new Vector3f(-1f, 1f, -1f);
        Vector3f v2 = new Vector3f(-1f, 0f, -1f);
        Vector3f v3 = new Vector3f(-1f, 0f, 1f);

        Vector3f crossProduct = Vector3f.cross(v1, v2);
        assertEquals(v3, crossProduct);
    }

    @Test
    void testCrossVector5() {
        Vector3f v1 = new Vector3f(1f, 2f, 3f);
        Vector3f v2 = new Vector3f(4f, -9f, 13f);
        Vector3f v3 = new Vector3f(53f, -1f, -17f);

        Vector3f crossProduct = Vector3f.cross(v1, v2);
        assertEquals(v3, crossProduct);
    }

    @Test
    void testVectorFail() {
        // assertEquals(1L, 2L);
    }

}
