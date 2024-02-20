package org.north.core.architecture.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.north.core.architecture.tree.Tree;
import org.north.core.architecture.tree.EntityTree;
import org.north.core.architecture.tree.TreeNodeLoopException;

import java.util.Iterator;

public class EntityTreeNodeHierarchyTest {

    private Tree<Entity> em;

    private Entity e1;
    private Entity e2;
    private Entity e3;
    private Entity e4;
    private Entity e5;
    private Entity e6;
    private Entity e7;
    private Entity e8;
    private Entity e9;

    @BeforeEach
    void initEntities() {
        em = new EntityTree();

        e1 = new Entity();
        e2 = new Entity();
        e3 = new Entity();
        e4 = new Entity();
        e5 = new Entity();
        e6 = new Entity();
        e7 = new Entity();
        e8 = new Entity();
        e9 = new Entity();
    }

    @Test
    void testIsParentOf() {
        e2.setParent(e1);

        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertFalse(e2.isParentOf(e2));
        Assertions.assertFalse(e2.isParentOf(e1));
        Assertions.assertFalse(e1.isParentOf(e1));
    }

    @Test
    void testGetParent() {
        e2.setParent(e1);

        Assertions.assertEquals(e2.getParent(), e1);
        Assertions.assertNotEquals(e2.getParent(), e2);
        Assertions.assertNotEquals(e1.getParent(), e2);
        Assertions.assertNotEquals(e1.getParent(), e1);
    }

    @Test
    void testGetDaughters() {
        e2.setParent(e1);

        Assertions.assertTrue(e1.getDaughters().contains(e2));
        Assertions.assertFalse(e2.getDaughters().contains(e2));
        Assertions.assertFalse(e2.getDaughters().contains(e1));
        Assertions.assertFalse(e1.getDaughters().contains(e1));
    }

    @Test
    void testGetRoot() {
        e2.setParent(e1);

        Assertions.assertEquals(e1, e2.getRoot());
        Assertions.assertEquals(e1, e1.getRoot());
        Assertions.assertNotEquals(e2, e2.getRoot());
        Assertions.assertNotEquals(e2, e1.getRoot());
    }

    @Test
    void testSetParent() {
        e2.setParent(e1);
        e3.setParent(e1);
        e4.setParent(e2);

        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertFalse(e2.isParentOf(e1));
        Assertions.assertTrue(e2.isParentOf(e4));
        Assertions.assertFalse(e3.isParentOf(e4));
        Assertions.assertFalse(e1.isParentOf(e4));
    }

    @Test
    void testSetParentFromTop() {
        // 1st level
        e2.setParent(e1);
        e3.setParent(e1);

        // 2nd level
        e7.setParent(e2);
        e4.setParent(e3);
        e5.setParent(e3);
        e6.setParent(e3);

        // 3rd level
        e8.setParent(e5);
        e9.setParent(e5);

        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertTrue(e1.isParentOf(e3));
        Assertions.assertTrue(e2.isParentOf(e7));
        Assertions.assertTrue(e3.isParentOf(e4));
        Assertions.assertTrue(e3.isParentOf(e5));
        Assertions.assertTrue(e3.isParentOf(e6));
        Assertions.assertTrue(e5.isParentOf(e8));
        Assertions.assertTrue(e5.isParentOf(e9));
        Assertions.assertTrue(e1.isAncestorOf(e2));
        Assertions.assertTrue(e1.isAncestorOf(e3));
        Assertions.assertTrue(e1.isAncestorOf(e4));
        Assertions.assertTrue(e1.isAncestorOf(e5));
        Assertions.assertTrue(e1.isAncestorOf(e6));
        Assertions.assertTrue(e1.isAncestorOf(e7));
        Assertions.assertTrue(e1.isAncestorOf(e8));
        Assertions.assertTrue(e1.isAncestorOf(e9));
        Assertions.assertFalse(e2.isParentOf(e1));
        Assertions.assertFalse(e3.isParentOf(e1));
        Assertions.assertFalse(e7.isParentOf(e2));
        Assertions.assertFalse(e4.isParentOf(e3));
        Assertions.assertFalse(e5.isParentOf(e3));
        Assertions.assertFalse(e6.isParentOf(e3));
        Assertions.assertFalse(e8.isParentOf(e5));
        Assertions.assertFalse(e9.isParentOf(e5));
        Assertions.assertFalse(e2.isAncestorOf(e1));
        Assertions.assertFalse(e3.isAncestorOf(e1));
        Assertions.assertFalse(e4.isAncestorOf(e1));
        Assertions.assertFalse(e5.isAncestorOf(e1));
        Assertions.assertFalse(e6.isAncestorOf(e1));
        Assertions.assertFalse(e7.isAncestorOf(e1));
        Assertions.assertFalse(e8.isAncestorOf(e1));
        Assertions.assertFalse(e9.isAncestorOf(e1));
    }

    @Test
    void testSetParentFromBottom() {
        // 3rd level
        e8.setParent(e5);
        e9.setParent(e5);

        // 2nd level
        e7.setParent(e2);
        e4.setParent(e3);
        e5.setParent(e3);
        e6.setParent(e3);

        // 1st level
        e2.setParent(e1);
        e3.setParent(e1);

        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertTrue(e1.isParentOf(e3));
        Assertions.assertTrue(e2.isParentOf(e7));
        Assertions.assertTrue(e3.isParentOf(e4));
        Assertions.assertTrue(e3.isParentOf(e5));
        Assertions.assertTrue(e3.isParentOf(e6));
        Assertions.assertTrue(e5.isParentOf(e8));
        Assertions.assertTrue(e5.isParentOf(e9));
        Assertions.assertTrue(e1.isAncestorOf(e2));
        Assertions.assertTrue(e1.isAncestorOf(e3));
        Assertions.assertTrue(e1.isAncestorOf(e4));
        Assertions.assertTrue(e1.isAncestorOf(e5));
        Assertions.assertTrue(e1.isAncestorOf(e6));
        Assertions.assertTrue(e1.isAncestorOf(e7));
        Assertions.assertTrue(e1.isAncestorOf(e8));
        Assertions.assertTrue(e1.isAncestorOf(e9));
        Assertions.assertFalse(e2.isParentOf(e1));
        Assertions.assertFalse(e3.isParentOf(e1));
        Assertions.assertFalse(e7.isParentOf(e2));
        Assertions.assertFalse(e4.isParentOf(e3));
        Assertions.assertFalse(e5.isParentOf(e3));
        Assertions.assertFalse(e6.isParentOf(e3));
        Assertions.assertFalse(e8.isParentOf(e5));
        Assertions.assertFalse(e9.isParentOf(e5));
        Assertions.assertFalse(e2.isAncestorOf(e1));
        Assertions.assertFalse(e3.isAncestorOf(e1));
        Assertions.assertFalse(e4.isAncestorOf(e1));
        Assertions.assertFalse(e5.isAncestorOf(e1));
        Assertions.assertFalse(e6.isAncestorOf(e1));
        Assertions.assertFalse(e7.isAncestorOf(e1));
        Assertions.assertFalse(e8.isAncestorOf(e1));
        Assertions.assertFalse(e9.isAncestorOf(e1));
    }

    @Test
    void testRearrange() {
        // 1st level
        e2.setParent(e1);
        e3.setParent(e1);

        // 2nd level
        e7.setParent(e2);
        e4.setParent(e3);
        e5.setParent(e3);
        e6.setParent(e3);

        // 3rd level
        e8.setParent(e5);
        e9.setParent(e5);


        // rearrange tree
        e5.setParent(e1);


        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertTrue(e1.isParentOf(e3));
        Assertions.assertTrue(e1.isParentOf(e5));
        Assertions.assertTrue(e2.isParentOf(e7));
        Assertions.assertTrue(e3.isParentOf(e4));
        Assertions.assertTrue(e3.isParentOf(e6));
        Assertions.assertTrue(e5.isParentOf(e8));
        Assertions.assertTrue(e5.isParentOf(e9));
        Assertions.assertTrue(e1.isAncestorOf(e2));
        Assertions.assertTrue(e1.isAncestorOf(e3));
        Assertions.assertTrue(e1.isAncestorOf(e4));
        Assertions.assertTrue(e1.isAncestorOf(e5));
        Assertions.assertTrue(e1.isAncestorOf(e6));
        Assertions.assertTrue(e1.isAncestorOf(e7));
        Assertions.assertTrue(e1.isAncestorOf(e8));
        Assertions.assertTrue(e1.isAncestorOf(e9));
        Assertions.assertFalse(e2.isParentOf(e1));
        Assertions.assertFalse(e3.isParentOf(e1));
        Assertions.assertFalse(e5.isParentOf(e1));
        Assertions.assertFalse(e7.isParentOf(e2));
        Assertions.assertFalse(e4.isParentOf(e3));
        Assertions.assertFalse(e6.isParentOf(e3));
        Assertions.assertFalse(e8.isParentOf(e5));
        Assertions.assertFalse(e9.isParentOf(e5));
        Assertions.assertFalse(e2.isAncestorOf(e1));
        Assertions.assertFalse(e3.isAncestorOf(e1));
        Assertions.assertFalse(e4.isAncestorOf(e1));
        Assertions.assertFalse(e5.isAncestorOf(e1));
        Assertions.assertFalse(e6.isAncestorOf(e1));
        Assertions.assertFalse(e7.isAncestorOf(e1));
        Assertions.assertFalse(e8.isAncestorOf(e1));
        Assertions.assertFalse(e9.isAncestorOf(e1));
    }

    @Test
    void testRearrangeToList() {
        // 1st level
        e2.setParent(e1);
        e3.setParent(e1);

        // 2nd level
        e7.setParent(e2);
        e4.setParent(e3);
        e5.setParent(e3);
        e6.setParent(e3);

        // 3rd level
        e8.setParent(e5);
        e9.setParent(e5);


        // rearrange tree to list-like form
        e2.setParent(e1);
        e3.setParent(e2);
        e4.setParent(e3);
        e5.setParent(e4);
        e6.setParent(e5);
        e7.setParent(e6);
        e8.setParent(e7);
        e9.setParent(e8);


        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertTrue(e2.isParentOf(e3));
        Assertions.assertTrue(e3.isParentOf(e4));
        Assertions.assertTrue(e4.isParentOf(e5));
        Assertions.assertTrue(e5.isParentOf(e6));
        Assertions.assertTrue(e6.isParentOf(e7));
        Assertions.assertTrue(e7.isParentOf(e8));
        Assertions.assertTrue(e8.isParentOf(e9));
        Assertions.assertTrue(e1.isAncestorOf(e2));
        Assertions.assertTrue(e1.isAncestorOf(e3));
        Assertions.assertTrue(e1.isAncestorOf(e4));
        Assertions.assertTrue(e1.isAncestorOf(e5));
        Assertions.assertTrue(e1.isAncestorOf(e6));
        Assertions.assertTrue(e1.isAncestorOf(e7));
        Assertions.assertTrue(e1.isAncestorOf(e8));
        Assertions.assertTrue(e1.isAncestorOf(e9));
        Assertions.assertFalse(e2.isParentOf(e1));
        Assertions.assertFalse(e3.isParentOf(e2));
        Assertions.assertFalse(e4.isParentOf(e3));
        Assertions.assertFalse(e5.isParentOf(e4));
        Assertions.assertFalse(e6.isParentOf(e5));
        Assertions.assertFalse(e7.isParentOf(e6));
        Assertions.assertFalse(e8.isParentOf(e7));
        Assertions.assertFalse(e9.isParentOf(e8));
        Assertions.assertFalse(e2.isAncestorOf(e1));
        Assertions.assertFalse(e3.isAncestorOf(e1));
        Assertions.assertFalse(e4.isAncestorOf(e1));
        Assertions.assertFalse(e5.isAncestorOf(e1));
        Assertions.assertFalse(e6.isAncestorOf(e1));
        Assertions.assertFalse(e7.isAncestorOf(e1));
        Assertions.assertFalse(e8.isAncestorOf(e1));
        Assertions.assertFalse(e9.isAncestorOf(e1));
    }

    @Test
    void testEntityLoopExceptionWhileSetParent1() {
        e2.setParent(e1);

        Assertions.assertThrows(TreeNodeLoopException.class, () -> e1.setParent(e2));
    }

    @Test
    void testEntityLoopExceptionWhileSetParent2() {
        e2.setParent(e1);
        e3.setParent(e1);
        e4.setParent(e2);

        Assertions.assertThrows(TreeNodeLoopException.class, () -> e2.setParent(e4));
        Assertions.assertThrows(TreeNodeLoopException.class, () -> e1.setParent(e2));
        Assertions.assertThrows(TreeNodeLoopException.class, () -> e1.setParent(e3));
    }

    @Test
    void testEntityLinkerWithRoot4() {
        e2.setParent(e1);
        e3.setParent(e1);
        e4.setParent(e2);
        e5.setParent(e2);
        e6.setParent(e2);
        e7.setParent(e6);
        e8.setParent(e7);

        Assertions.assertEquals(e1, e1.getRoot());
        Assertions.assertEquals(e1, e2.getRoot());
        Assertions.assertEquals(e1, e3.getRoot());
        Assertions.assertEquals(e1, e4.getRoot());
        Assertions.assertEquals(e1, e5.getRoot());
        Assertions.assertEquals(e1, e6.getRoot());
        Assertions.assertEquals(e1, e7.getRoot());
        Assertions.assertEquals(e1, e8.getRoot());
    }

    @Test
    void testEntityAncestorCheck1() {
        e2.setParent(e1);
        e3.setParent(e2);

        Assertions.assertTrue(e1.isAncestorOf(e2));
        Assertions.assertTrue(e1.isAncestorOf(e3));
        Assertions.assertFalse(e1.isAncestorOf(e1));
        Assertions.assertFalse(e2.isAncestorOf(e1));
        Assertions.assertFalse(e2.isAncestorOf(e2));
        Assertions.assertFalse(e3.isAncestorOf(e1));
        Assertions.assertFalse(e3.isAncestorOf(e2));
        Assertions.assertFalse(e3.isAncestorOf(e3));
    }

    @Test
    void testEntityTreeEntityCreation1() {
        Entity e1 = em.create("e1");
        Entity e2 = em.create("e2");

        Assertions.assertEquals(e1, e2.getParent());
        Assertions.assertNotEquals(e2, e1.getParent());
        Assertions.assertNotEquals(e1, e1.getParent());
        Assertions.assertNotEquals(e2, e2.getParent());
    }

    @Test
    void testEntityTreeWithSetParent1() {
        Entity e1 = em.create("e1");
        Entity e2 = em.create("e2");

        e2.setParent(e1);

        Assertions.assertEquals(e1, e2.getParent());
        Assertions.assertNotEquals(e2, e1.getParent());
        Assertions.assertNotEquals(e1, e1.getParent());
        Assertions.assertNotEquals(e2, e2.getParent());
    }

    @Test
    void testEntityTreeWithSetParent2() {
        Entity e1 = em.create("e1");
        Entity e2 = em.create("e2");

        e2.setParent(e1);

        Entity e3 = em.create("e3");
        Entity e4 = em.create("e4");

        Assertions.assertNull(e1.getParent());
        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertTrue(e1.isParentOf(e3));
        Assertions.assertTrue(e1.isParentOf(e4));
        Assertions.assertFalse(e1.getDaughters().isEmpty());
        Assertions.assertEquals(3, e1.getDaughters().size());
        Assertions.assertEquals(e1, e2.getParent());
        Assertions.assertNotEquals(e2, e1.getParent());
        Assertions.assertNotEquals(e1, e1.getParent());
        Assertions.assertNotEquals(e2, e2.getParent());
    }

    @Test
    void testEntityTreeWithSetParent3() {
        Entity e1 = em.create("e1");
        Entity e2 = em.create(e1, "e2");
        Entity e3 = em.create("e3");
        Entity e4 = em.create("e4");

        Assertions.assertNull(e1.getParent());
        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertTrue(e1.isParentOf(e3));
        Assertions.assertTrue(e1.isParentOf(e4));
        Assertions.assertFalse(e1.getDaughters().isEmpty());
        Assertions.assertEquals(3, e1.getDaughters().size());
        Assertions.assertEquals(e1, e2.getParent());
        Assertions.assertNotEquals(e2, e1.getParent());
        Assertions.assertNotEquals(e1, e1.getParent());
        Assertions.assertNotEquals(e2, e2.getParent());
    }

    @Test
    void testEntityTreeTraversal() {
        e1 = em.create("e1");

        // 1st level
        e2 = em.create(e1, "e2");
        e3 = em.create(e1, "e3");

        // 2nd level
        e7 = em.create(e2, "e7");
        e4 = em.create(e3, "e4");
        e5 = em.create(e3, "e5");
        e6 = em.create(e3, "e6");

        // 3rd level
        e8 = em.create(e5, "e8");
        e9 = em.create(e5, "e9");


        Iterator<Entity> iterator = em.iterator();

        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertSame(e7, iterator.next());
        Assertions.assertSame(e2, iterator.next());
        Assertions.assertSame(e4, iterator.next());
        Assertions.assertSame(e8, iterator.next());
        Assertions.assertSame(e9, iterator.next());
        Assertions.assertSame(e5, iterator.next());
        Assertions.assertSame(e6, iterator.next());
        Assertions.assertSame(e3, iterator.next());
        Assertions.assertSame(e1, iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

}
