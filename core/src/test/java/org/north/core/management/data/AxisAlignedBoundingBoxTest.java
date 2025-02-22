package org.north.core.management.data;

import org.joml.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AxisAlignedBoundingBoxTest {

    @Test
    void isIntersects() {
        AABB aabb1 = new AABB(new Vector3f(0, 0, 0), new Vector3f(2, 2, 2));
        AABB aabb2 = new AABB(new Vector3f(1, 1, 1), new Vector3f(3, 3, 3));
        AABB aabb3 = new AABB(new Vector3f(3, 3, 3), new Vector3f(5, 5, 5));

        AABB aabb4 = new AABB(new Vector3f(0, 0, 0), new Vector3f(2, 0, 2));
        AABB aabb5 = new AABB(new Vector3f(0.5f, -1, 0), new Vector3f(1.5f, 1, 0));

        Assertions.assertTrue(aabb1.isIntersects(aabb2));
        Assertions.assertTrue(aabb2.isIntersects(aabb1));
        Assertions.assertFalse(aabb1.isIntersects(aabb3));
        Assertions.assertFalse(aabb3.isIntersects(aabb1));
        Assertions.assertTrue(aabb4.isIntersects(aabb5));
        Assertions.assertTrue(aabb5.isIntersects(aabb4));
    }

    @Test
    void isInside() {
        AABB aabb1 = new AABB(new Vector3f(0, 0, 0), new Vector3f(2, 2, 2));
        AABB aabb2 = new AABB(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

        AABB aabb4 = new AABB(new Vector3f(0, 0, 0), new Vector3f(2, 0, 2));
        AABB aabb5 = new AABB(new Vector3f(0.5f, -1, 0), new Vector3f(1.5f, 1, 0));

        Assertions.assertTrue(aabb2.isInside(aabb1));
        Assertions.assertFalse(aabb1.isInside(aabb2));
        Assertions.assertFalse(aabb4.isInside(aabb5));
        Assertions.assertFalse(aabb5.isInside(aabb4));
    }

    @Test
    void isInsideAndIntersects() {
        AABB aabb1 = new AABB(new Vector3f(-2, -2, -2), new Vector3f(2, 2, 2));
        AABB aabb2 = new AABB(new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));

        Assertions.assertFalse(aabb1.isInside(aabb2));
        Assertions.assertTrue(aabb2.isInside(aabb1));
        Assertions.assertTrue(aabb1.isIntersects(aabb2));
        Assertions.assertTrue(aabb2.isIntersects(aabb1));
    }
}