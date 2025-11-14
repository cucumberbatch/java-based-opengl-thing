package org.north.core.management.data;

import org.joml.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

class OctreeNodeTest {
    private final Logger logger = LoggerFactory.getLogger(OctreeNodeTest.class);

    @Test
    void addElement() {
        OctreeNode<AABB> octree =
                new OctreeNode<>(new Vector3f(-2, -2, -2), new Vector3f(2, 2, 2));

        octree.insert(new AABB(new Vector3f(-1.2f, -1.2f, -1.2f), new Vector3f(-1, -1, -1)));
        octree.insert(new AABB(new Vector3f(-1.8f, -1.8f, -1.8f), new Vector3f(-1.7f, -1.7f, -1.7f)));
        octree.insert(new AABB(new Vector3f(1.5f, 1.5f, 1.5f), new Vector3f(1.7f, 1.7f, 1.7f)));
        octree.insert(new AABB(new Vector3f(0.2f, 0.2f, -0.5f), new Vector3f(0.4f, 0.4f, 0f)));
        octree.insert(new AABB(new Vector3f(-0.5f, 0f, -0.5f), new Vector3f(0f, 0.5f, 0f)));
        octree.insert(new AABB(new Vector3f(0.5f, 0.5f, 0f), new Vector3f(0.7f, 0.7f, 0.2f)));
    }

    @Test
    void add() {
        OctreeNode<AABB> octree =
                new OctreeNode<>(new Vector3f(-2, -2, -2), new Vector3f(2, 2, 2));

        octree.insert(new AABB(new Vector3f(-1.2f, -1.2f, -1.2f), new Vector3f(-1, -1, -1)));
        octree.insert(new AABB(new Vector3f(-1.8f, -1.8f, -1.8f), new Vector3f(-1.7f, -1.7f, -1.7f)));
        octree.insert(new AABB(new Vector3f(1.5f, 1.5f, 1.5f), new Vector3f(1.7f, 1.7f, 1.7f)));
        octree.insert(new AABB(new Vector3f(0.2f, 0.2f, -0.5f), new Vector3f(0.4f, 0.4f, 0f)));
        octree.insert(new AABB(new Vector3f(-0.5f, 0f, -0.5f), new Vector3f(0f, 0.5f, 0f)));
        octree.insert(new AABB(new Vector3f(0.5f, 0.5f, 0f), new Vector3f(0.7f, 0.7f, 0.2f)));
    }

    @Test
    void addElementInAllNodes() {
        OctreeNode<AABB> octree =
                new OctreeNode<>(new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));

        octree.insert(new AABB(new Vector3f(-0.9f, -0.9f, -0.9f), new Vector3f(-.1f, -.1f, -.1f)));
        octree.insert(new AABB(new Vector3f(.1f, -0.9f, -0.9f), new Vector3f(0.9f, .1f, .1f)));
        octree.insert(new AABB(new Vector3f(-0.49f, .1f, -0.49f), new Vector3f(.1f, 0.49f, .1f)));
        octree.insert(new AABB(new Vector3f(-0.9f, .1f, -0.9f), new Vector3f(.1f, 0.9f, .1f)));
        octree.insert(new AABB(new Vector3f(-0.9f, -0.9f, .1f), new Vector3f(.1f, .1f, 0.9f)));
    }

    @Test
    void iteratorTest() {
        OctreeNode<AxisAlignedBoundingBox> octree =
                new OctreeNode<>(new Vector3f(-1000, -1000, -1000),
                        new Vector3f(1000,
                                1000, 1000));

        AABB e1 = new AABB(new Vector3f(10, 10, 10), new Vector3f(20, 20, 20));
        AABB e2 = new AABB(new Vector3f(30, 30, 30), new Vector3f(40, 40, 40));
        AABB e3 = new AABB(new Vector3f(50, 50, 50), new Vector3f(60, 60, 60));

        Set<AxisAlignedBoundingBox> set1 = Set.of(e1, e2, e3);
        AABB query1 = new AABB(new Vector3f(15, 15, 15), new Vector3f(55, 55, 55));

        Set<AxisAlignedBoundingBox> set2 = Set.of(e2, e3);
        AABB query2 = new AABB(new Vector3f(25, 30, 35), new Vector3f(55, 55, 55));

        Set<AxisAlignedBoundingBox> set3 = Set.of(e1);
        AABB query3 = new AABB(new Vector3f(11, 11, 11), new Vector3f(13, 13, 13));

        octree.insert(e1);
        octree.insert(e2);
        octree.insert(e3);

        Set<AxisAlignedBoundingBox> resultSet = new HashSet<>();
        for (AxisAlignedBoundingBox obj : octree.query(query1)) {
            resultSet.add(obj);
        }
        Assertions.assertEquals(set1, resultSet);

        resultSet.clear();
        for (AxisAlignedBoundingBox obj : octree.query(query2)) {
            resultSet.add(obj);
        }
        Assertions.assertEquals(set2, resultSet);

        resultSet.clear();
        for (AxisAlignedBoundingBox obj : octree.query(query3)) {
            resultSet.add(obj);
        }
        Assertions.assertEquals(set3, resultSet);

        logger.debug("Result tree: {}", octree);
    }
}