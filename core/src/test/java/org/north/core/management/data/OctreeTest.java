package org.north.core.management.data;

import org.joml.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

class OctreeTest {
    private final Logger logger = LoggerFactory.getLogger(OctreeTest.class);

    @Test
    void addElement() {
        Octree<AABB> octree = new Octree<>(new Vector3f(-2, -2, -2), new Vector3f(2, 2, 2));

        octree.add(new AABB(new Vector3f(-1.2f, -1.2f, -1.2f), new Vector3f(-1.0f, -1.0f, -1.0f)));
        octree.add(new AABB(new Vector3f(-1.8f, -1.8f, -1.8f), new Vector3f(-1.7f, -1.7f, -1.7f)));
        octree.add(new AABB(new Vector3f( 1.5f,  1.5f,  1.5f), new Vector3f( 1.7f,  1.7f,  1.7f)));
        octree.add(new AABB(new Vector3f( 0.2f,  0.2f, -0.5f), new Vector3f( 0.4f,  0.4f,  0.0f)));
        octree.add(new AABB(new Vector3f(-0.5f,  0.0f, -0.5f), new Vector3f( 0.0f,  0.5f,  0.0f)));
        octree.add(new AABB(new Vector3f( 0.5f,  0.5f,  0.0f), new Vector3f( 0.7f,  0.7f,  0.2f)));

        Assertions.assertEquals(6, octree.size());
    }

    @Test
    void iteratorTest() {
        Octree<AABB> octree = new Octree<>(new Vector3f(-100, -100, -100), new Vector3f(100, 100, 100));

        AABB e1 = new AABB(new Vector3f(10, 10, 10), new Vector3f(20, 20, 20));
        AABB e2 = new AABB(new Vector3f(30, 30, 30), new Vector3f(40, 40, 40));
        AABB e3 = new AABB(new Vector3f(50, 50, 50), new Vector3f(60, 60, 60));
        AABB e4 = new AABB(new Vector3f(-30, -30, -30), new Vector3f(-20, -20, -20));

        octree.add(e1);
        octree.add(e2);
        octree.add(e3);
        octree.add(e4);

        Assertions.assertEquals(4, octree.size());

        AABB query1 = new AABB(new Vector3f(15, 15, 15), new Vector3f(45, 45, 45));
        Set<AxisAlignedBoundingBox> expected1 = Set.of(e1, e2);

        Set<AxisAlignedBoundingBox> result1 = new HashSet<>();
        for (AxisAlignedBoundingBox obj : octree.query(query1)) {
            result1.add(obj);
        }

        Assertions.assertEquals(expected1, result1, "Query should return e1 and e2");

        AABB query2 = new AABB(new Vector3f(35, 35, 35), new Vector3f(55, 55, 55));
        Set<AxisAlignedBoundingBox> expected2 = Set.of(e2, e3);

        Set<AxisAlignedBoundingBox> result2 = new HashSet<>();
        for (AxisAlignedBoundingBox obj : octree.query(query2)) {
            result2.add(obj);
        }

        Assertions.assertEquals(expected2, result2, "Query should return e2 and e3");

        AABB query3 = new AABB(new Vector3f(12, 12, 12), new Vector3f(18, 18, 18));
        Set<AxisAlignedBoundingBox> expected3 = Set.of(e1);

        Set<AxisAlignedBoundingBox> result3 = new HashSet<>();
        for (AxisAlignedBoundingBox obj : octree.query(query3)) {
            result3.add(obj);
        }

        Assertions.assertEquals(expected3, result3, "Query should return only e1");

        AABB query4 = new AABB(new Vector3f(80, 80, 80), new Vector3f(90, 90, 90));

        Set<AxisAlignedBoundingBox> result4 = new HashSet<>();
        for (AxisAlignedBoundingBox obj : octree.query(query4)) {
            result4.add(obj);
        }

        Assertions.assertTrue(result4.isEmpty(), "Query should return no elements");

        AABB query5 = new AABB(new Vector3f(-50, -50, -50), new Vector3f(70, 70, 70));
        Set<AxisAlignedBoundingBox> expected5 = Set.of(e1, e2, e3, e4);

        Set<AxisAlignedBoundingBox> result5 = new HashSet<>();
        for (AxisAlignedBoundingBox obj : octree.query(query5)) {
            result5.add(obj);
        }

        Assertions.assertEquals(expected5, result5, "Query should return all elements");

    }

    @Test
    void addElementWithTreeStructure() {
        Octree<AABB> octree = new Octree<>(new Vector3f(-2, -2, -2), new Vector3f(2, 2, 2));

        AABB element1 = new AABB(new Vector3f(-1.2f, -1.2f, -1.2f), new Vector3f(-1, -1, -1));
        octree.add(element1);

        AABB element2 = new AABB(new Vector3f(-1.8f, -1.8f, -1.8f), new Vector3f(-1.7f, -1.7f, -1.7f));
        octree.add(element2);

        AABB element3 = new AABB(new Vector3f(1.5f, 1.5f, 1.5f), new Vector3f(1.7f, 1.7f, 1.7f));
        octree.add(element3);

        AABB element4 = new AABB(new Vector3f(0.2f, 0.2f, -0.5f), new Vector3f(0.4f, 0.4f, 0f));
        octree.add(element4);

        AABB element5 = new AABB(new Vector3f(-0.5f, 0f, -0.5f), new Vector3f(0f, 0.5f, 0f));
        octree.add(element5);

        AABB element6 = new AABB(new Vector3f(0.5f, 0.5f, 0f), new Vector3f(0.7f, 0.7f, 0.2f));
        octree.add(element6);

        Assertions.assertEquals(6, octree.size());
    }

}