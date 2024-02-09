package core.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.north.core.entities.Entity;

public class EntityTreeNodeHierarchyTest {

    @Test
    void testIsParentOfBasic() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();

        e2.setParent(e1);

        Assertions.assertTrue(e1.isParentOf(e2));
        Assertions.assertFalse(e2.isParentOf(e2));
        Assertions.assertFalse(e2.isParentOf(e1));
        Assertions.assertFalse(e1.isParentOf(e1));
    }

    @Test
    void testGetParentBasic() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();

        e2.setParent(e1);

        Assertions.assertEquals(e2.getParent(), e1);
        Assertions.assertNotEquals(e2.getParent(), e2);
        Assertions.assertNotEquals(e1.getParent(), e2);
        Assertions.assertNotEquals(e1.getParent(), e1);
    }

    @Test
    void testGetDaughtersBasic() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();

        e2.setParent(e1);

        Assertions.assertTrue(e1.getDaughters().contains(e2));
        Assertions.assertFalse(e2.getDaughters().contains(e2));
        Assertions.assertFalse(e2.getDaughters().contains(e1));
        Assertions.assertFalse(e1.getDaughters().contains(e1));
    }

    @Test
    void testEntityHierarchy2() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();

        e2.setParent(e1);
        e1.setParent(e2);

        Assertions.assertTrue(e2.getDaughters().contains(e1));
        Assertions.assertEquals(e1.getParent(), e2);
    }

    @Test
    void testGetRootBasic() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();

        e2.setParent(e1);

        Assertions.assertEquals(e1, e2.getRoot());
        Assertions.assertEquals(e1, e1.getRoot());
        Assertions.assertNotEquals(e2, e2.getRoot());
        Assertions.assertNotEquals(e2, e1.getRoot());
    }

    @Test
    void testEntityLinkerWithRoot2() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();

        e2.setParent(e1);
        e1.setParent(e2);

        Assertions.assertEquals(e2, e2.getRoot());
    }

    @Test
    void testEntityLinkerWithRoot3() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();
        Entity e3 = new Entity();
        Entity e4 = new Entity();

        e2.setParent(e1);
        e3.setParent(e1);
        e4.setParent(e2);
        e2.setParent(e4);

        Assertions.assertEquals(e1, e1.getRoot());
        Assertions.assertEquals(e4, e2.getRoot());
        Assertions.assertEquals(e1, e3.getRoot());
        Assertions.assertEquals(e4, e4.getRoot());
    }

    @Test
    void testEntityLinkerWithRoot4() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();
        Entity e3 = new Entity();
        Entity e4 = new Entity();
        Entity e5 = new Entity();
        Entity e6 = new Entity();
        Entity e7 = new Entity();
        Entity e8 = new Entity();

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

//    @Test
    void testEntityLinkerWithParentCheck1() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();
        Entity e3 = new Entity();
        Entity e4 = new Entity();

        e2.setParent(e1);
        e3.setParent(e1);
        e4.setParent(e2);

        Assertions.assertTrue(e2.isParentOf(e1));
        Assertions.assertFalse(e1.isParentOf(e2));
        Assertions.assertTrue(e4.isParentOf(e2));
        Assertions.assertFalse(e4.isParentOf(e3));
        Assertions.assertFalse(e4.isParentOf(e1));
    }

}
