package org.north.core.architecture.tree.v2;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public interface TreeNode<Node extends TreeNode<Node>> extends Iterable<Node>, Serializable {

    /**
     * Checks if the current node does not have any subtrees
     * @return true if the current node has no children, false otherwise
     */
    boolean isLeaf();

    /**
     * Returns a parent node relative to the current node
     * @return parent node
     */
    Node getParent();

    boolean hasNextSibling();

    Node getNextSibling();

    Node getPreviousSibling(Node subtree);

    boolean hasPreviousSibling();

    Node getLeftMostDaughter();

    boolean add(Node subtree);

    boolean remove(Node subtree);

    Collection<Node> getSubtrees();

    /**
     * Returns the root node relative to the current node
     * @return root node relative to the current node
     */
    @SuppressWarnings("unchecked")
    default Node getRoot() {
        if (isRoot()) {
            return (Node) this;
        }
        Node root = getParent();
        while (!root.isRoot()) {
            root = root.getParent();
        }
        return root;
    }

    /**
     * Returns the size of the subtree rooted at the current node
     * @return size of a subtree
     */
    default int size() {
        final int[] counter = new int[]{0};
        traversePreorder(node -> counter[0]++);
        return counter[0];
    }

    /**
     * Checks whether there is a parent node for the specified node
     * @return true if there is no parent node, false - otherwise
     */
    default boolean isRoot() {
        return getParent() == null;
    }

    /**
     * Checks whether the current node is the parent of a given subtree
     * @param subtree a given subtree node
     * @return a boolean value that indicates whether the current node
     * is the parent of a given subtree node. If the subtree is not null and
     * the current node equals its parent, the method will return true.
     * Otherwise, it will return false.
     */
    default boolean isParentOf(Node subtree) {
        return subtree != null && this.equals(subtree.getParent());
    }

    default boolean isAncestorOf(Node subtree) {
        if (subtree == null || subtree.isRoot()) {
            return false;
        }
        Node parent = subtree;
        do {
            if (this.equals(parent)) {
                return true;
            }
            parent = parent.getRoot();
        } while (!parent.isRoot());
        return false;
    }

    default boolean isDescendantOf(Node subtree) {
        return subtree != null && this.isAncestorOf(subtree);
    }

    @SuppressWarnings("unchecked")
    default Node find(Predicate<Node> predicate) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push((Node) this);
        while (!stack.isEmpty()) {
            Node poppedNode = stack.pop();
            if (predicate.test(poppedNode))
                return poppedNode;
            for (Node descentSubtree : poppedNode.getSubtrees()) {
                stack.push(descentSubtree);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    default void ascendantTraverse(Action<Node> action) {
        Node currentNode = (Node) this;
        action.execute(currentNode);
        while (!currentNode.isRoot() && !action.isStoppingConditionSatisfied()) {
            currentNode = currentNode.getParent();
            action.execute(currentNode);
        }
    }

    @SuppressWarnings("unchecked")
    default void traversePreorder(Action<Node> action) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push((Node) this);
        while (!stack.isEmpty() && !action.isStoppingConditionSatisfied()) {
            Node poppedNode = stack.pop();
            action.execute(poppedNode);
            for (Node descentSubtree : poppedNode.getSubtrees()) {
                stack.push(descentSubtree);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    default Iterator<Node> iterator() {
        return new TreeNodeIterator<Node>((Node) this);
    }

    class TreeNodeIterator<Node extends TreeNode<Node>> implements Iterator<Node> {
        private final Deque<Node> traversalStack;

        public TreeNodeIterator(Node subtree) {
            this.traversalStack = new ArrayDeque<>(subtree.size());
            this.traversalStack.push(subtree);
        }

        @Override
        public boolean hasNext() {
            return !traversalStack.isEmpty();
        }

        @Override
        public Node next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node poppedNode = traversalStack.pop();
            for (Node descentNode : poppedNode.getSubtrees()) {
                traversalStack.push(descentNode);
            }
            return poppedNode;
        }

    }
}
