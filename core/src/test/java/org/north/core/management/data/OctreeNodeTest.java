package org.north.core.management.data;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

class OctreeNodeTest {

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
        OctreeNode<AABB> octree =
                new OctreeNode<>(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));

        AABB a1 = new AABB(new Vector3f(.1f, .5f, .5f), new Vector3f(.2f, .6f, .6f));
        AABB a2 = new AABB(new Vector3f(.4f, 5f, .4f), new Vector3f(.6f, 5.2f, .6f));
        AABB a3 = new AABB(new Vector3f(3f, 3f, 2f), new Vector3f(4f, 4f, 3f));
        AABB a4 = new AABB(new Vector3f(2f, .5f, 2f), new Vector3f(2.5f, 1f, 2.5f));

        AABB queryBox = new AABB(new Vector3f(2, 2, 2), new Vector3f(3, 3, 3));

        octree.insert(a1);
        octree.insert(a2);
        octree.insert(a3);
        octree.insert(a4);

        for (AABB aabb : octree.query(queryBox)) {
            System.out.println(aabb);
        }
    }

    @Test
    void iterator2Test() {
        // Создаём корневой узел октодерева
//        AABB rootBounds = new AABB(new Vector3f(0, 0, 0), new Vector3f(100, 100, 100));
//        OctreeNode octree = new OctreeNode(rootBounds, 0, 5, 10);
//
//        // Вставляем объекты
//        octree.insert(new AABB(10, 10, 10, 20, 20, 20));
//        octree.insert(new AABB(30, 30, 30, 40, 40, 40));
//        octree.insert(new AABB(50, 50, 50, 60, 60, 60));
//
//        // Выполняем запрос с использованием ленивого итератора
//        AABB queryBox = new AABB(15, 15, 15, 55, 55, 55);
//        Iterable<AxisAlignedBoundingBox> result = octree.query(queryBox);
//
//        // Используем ленивую коллекцию
//        for (AxisAlignedBoundingBox obj : result) {
//            System.out.println("Found object: " + obj);
//        }
    }
}