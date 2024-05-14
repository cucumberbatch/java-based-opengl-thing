package org.north.core.architecture.tree.v2;

public interface Action<E extends TreeNode<E>> {
    void execute(E node);

    default boolean isStoppingConditionSatisfied() {
        return false;
    }
}
