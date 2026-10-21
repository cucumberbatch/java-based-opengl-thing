package org.north.core.physics.spatial;

import org.joml.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AabbTest {

    private static final float EPSILON = 0.0001f;

    @Test
    void rayIntersectsBox() {
        Aabb box = new Aabb(-1f, 1f, -1f, 1f, -1f, 1f);

        float distance = box.intersectRay(
                new Vector3f(-2f, 0f, 0f),
                new Vector3f(1f, 0f, 0f),
                100f
        );

        Assertions.assertEquals(1f, distance, EPSILON);
    }

    @Test
    void rayMissesBox() {
        Aabb box = new Aabb(-1f, 1f, -1f, 1f, -1f, 1f);

        float distance = box.intersectRay(
                new Vector3f(-2f, 2f, 0f),
                new Vector3f(1f, 0f, 0f),
                100f
        );

        Assertions.assertEquals(Float.POSITIVE_INFINITY, distance);
    }

    @Test
    void rayFromInsideBoxStartsAtZero() {
        Aabb box = new Aabb(-1f, 1f, -1f, 1f, -1f, 1f);

        float distance = box.intersectRay(
                new Vector3f(0f, 0f, 0f),
                new Vector3f(1f, 0f, 0f),
                100f
        );

        Assertions.assertEquals(0f, distance, EPSILON);
    }

    @Test
    void rayBeyondMaximumDistanceDoesNotHit() {
        Aabb box = new Aabb(10f, 11f, -1f, 1f, -1f, 1f);

        float distance = box.intersectRay(
                new Vector3f(0f, 0f, 0f),
                new Vector3f(1f, 0f, 0f),
                5f
        );

        Assertions.assertEquals(Float.POSITIVE_INFINITY, distance);
    }

    @Test
    void boxFromPointsCoversBothPoints() {
        Aabb box = new Aabb().setFromPoints(
                new Vector3f(2f, -1f, 4f),
                new Vector3f(-3f, 5f, 1f)
        );

        Assertions.assertEquals(-3f, box.xMin(), EPSILON);
        Assertions.assertEquals(2f, box.xMax(), EPSILON);
        Assertions.assertEquals(-1f, box.yMin(), EPSILON);
        Assertions.assertEquals(5f, box.yMax(), EPSILON);
        Assertions.assertEquals(1f, box.zMin(), EPSILON);
        Assertions.assertEquals(4f, box.zMax(), EPSILON);
    }
}
