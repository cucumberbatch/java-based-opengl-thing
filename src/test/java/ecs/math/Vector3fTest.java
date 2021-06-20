package ecs.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class Vector3fTest {

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

}
