package ecs.entities;

import java.util.List;

public interface TreeNode<E extends TreeNode<E>> {
    long getId();
    String getName();
    E getParent();
    List<E> getDaughters();
    List<E> getSiblings();

    void setId(long id);
    void setName(String name);
    void setParent(E parent);
    void setDaughters(List<E> daughters);

    boolean isParentOf(E node);
}
