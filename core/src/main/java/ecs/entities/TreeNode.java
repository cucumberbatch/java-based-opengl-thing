package ecs.entities;

import java.util.List;

public interface TreeNode {
    long getId();
    String getName();
    TreeNode getParent();
    List<? extends TreeNode> getDaughters();

    void setId(long id);
    void setName(String name);
    void setParent(TreeNode parent);
    void setDaughters(List<? extends TreeNode> daughters);
}
