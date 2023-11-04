package ecs.entities;

import ecs.architecture.*;
import ecs.entities.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TreeEntityManagerTest {

    @Test
    void test2EntityHierarchy1() {
        TreeEntityManager entityManager = new TreeEntityManager();

        Entity e1 = entityManager.createEntity();
        Entity e2 = entityManager.createEntity();

        entityManager.linkWithParent(e1, e2);

        Assertions.assertTrue(e1.daughters.contains(e2));
        Assertions.assertEquals(e2.parent, e1);
    }

    @Test
    void test2EntityHierarchy2() {
        TreeEntityManager entityManager = new TreeEntityManager();

        Entity e1 = entityManager.createEntity();
        Entity e2 = entityManager.createEntity();

        entityManager.linkWithParent(e1, e2);
        entityManager.linkWithParent(e2, e1);

        Assertions.assertTrue(e2.daughters.contains(e1));
        Assertions.assertEquals(e1.parent, e2);
    }

    @Test
    void testEntityLinkerWithRoot1() {
        TreeEntityManager entityManager = new TreeEntityManager();

        Entity e1 = entityManager.createEntity();
        Entity e2 = entityManager.createEntity();

        entityManager.linkWithParent(e1, e2);

        Assertions.assertEquals(e1, entityManager.getRoot(e2));
    }

    @Test
    void testEntityLinkerWithRoot2() {
        TreeEntityManager entityManager = new TreeEntityManager();

        Entity e1 = entityManager.createEntity();
        Entity e2 = entityManager.createEntity();

        entityManager.linkWithParent(e1, e2);
        entityManager.linkWithParent(e2, e1);

        Assertions.assertEquals(e2, entityManager.getRoot(e2));
    }

    @Test
    void testEntityLinkerWithRoot3() {
        TreeEntityManager entityManager = new TreeEntityManager();

        Entity e1 = entityManager.createEntity();
        Entity e2 = entityManager.createEntity();
        Entity e3 = entityManager.createEntity();
        Entity e4 = entityManager.createEntity();

        entityManager.linkWithParent(e1, e2);
        entityManager.linkWithParent(e1, e3);
        entityManager.linkWithParent(e2, e4);
        entityManager.linkWithParent(e4, e2);

        Assertions.assertEquals(e1, entityManager.getRoot(e1));
        Assertions.assertEquals(e4, entityManager.getRoot(e2));
        Assertions.assertEquals(e1, entityManager.getRoot(e3));
        Assertions.assertEquals(e4, entityManager.getRoot(e4));
    }

    @Test
    void testEntityLinkerWithRoot4() {
        TreeEntityManager entityManager = new TreeEntityManager();

        Entity e1 = entityManager.createEntity();
        Entity e2 = entityManager.createEntity();
        Entity e3 = entityManager.createEntity();
        Entity e4 = entityManager.createEntity();
        Entity e5 = entityManager.createEntity();
        Entity e6 = entityManager.createEntity();
        Entity e7 = entityManager.createEntity();
        Entity e8 = entityManager.createEntity();

        entityManager.linkWithParent(e1, e2);
        entityManager.linkWithParent(e1, e3);
        entityManager.linkWithParent(e2, e4);
        entityManager.linkWithParent(e2, e5);
        entityManager.linkWithParent(e2, e6);
        entityManager.linkWithParent(e6, e7);
        entityManager.linkWithParent(e7, e8);

        Assertions.assertEquals(e1, entityManager.getRoot(e1));
        Assertions.assertEquals(e1, entityManager.getRoot(e2));
        Assertions.assertEquals(e1, entityManager.getRoot(e3));
        Assertions.assertEquals(e1, entityManager.getRoot(e4));
        Assertions.assertEquals(e1, entityManager.getRoot(e5));
        Assertions.assertEquals(e1, entityManager.getRoot(e6));
        Assertions.assertEquals(e1, entityManager.getRoot(e7));
        Assertions.assertEquals(e1, entityManager.getRoot(e8));
    }

//    @Test
    void testEntityLinkerWithParentCheck1() {
        TreeEntityManager entityManager = new TreeEntityManager();

        Entity e1 = entityManager.createEntity();
        Entity e2 = entityManager.createEntity();
        Entity e3 = entityManager.createEntity();
        Entity e4 = entityManager.createEntity();

        entityManager.linkWithParent(e1, e2);
        entityManager.linkWithParent(e1, e3);
        entityManager.linkWithParent(e2, e4);

        Assertions.assertTrue(entityManager.isParent(e2, e1));
        Assertions.assertFalse(entityManager.isParent(e1, e2));
        Assertions.assertTrue(entityManager.isParent(e4, e2));
        Assertions.assertFalse(entityManager.isParent(e4, e3));
        Assertions.assertFalse(entityManager.isParent(e4, e1));
    }

}
