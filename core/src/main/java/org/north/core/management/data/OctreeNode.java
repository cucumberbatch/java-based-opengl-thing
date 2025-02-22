package org.north.core.management.data;

import org.joml.Vector3f;

import java.util.*;

/**
 * A simple OctreeNode data structure implementation
 */
public class OctreeNode<E extends AxisAlignedBoundingBox> implements AxisAlignedBoundingBox {
    private static final byte DEFAULT_DENSITY_FACTOR = 2;
    private static final byte DEFAULT_MAX_DEPTH      = 2;

    private static final byte X_BIT = 0x1;
    private static final byte Y_BIT = 0x2;
    private static final byte Z_BIT = 0x4;

    // Bounds of a node
    private float xMin, xMax, yMin, yMax, zMin, zMax;

    private OctreeNode<E>   parentNode;
    private OctreeNode<E>[] childNodes;
    private List<E>         elements;
    private byte            maxElementsCount;
    private byte            depth;
    private byte            maxDepth;

    public OctreeNode(Vector3f nearBottomLeftPoint, Vector3f farTopRightPoint) {
        this(nearBottomLeftPoint.x, farTopRightPoint.x, nearBottomLeftPoint.y, farTopRightPoint.y, nearBottomLeftPoint.z, farTopRightPoint.z, null, (byte) 0);
    }

    public OctreeNode(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        this(xMin, xMax, yMin, yMax, zMin, zMax, null, (byte) 0);
    }

    private OctreeNode(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, OctreeNode<E> parentNode, byte depth) {
        setBounds(xMin, xMax, yMin, yMax, zMin, zMax);
        this.elements = new ArrayList<>(DEFAULT_DENSITY_FACTOR);
        this.parentNode = parentNode;
        this.depth = depth;
    }

    /**
     * Inserts a provided element into this node or child nodes this node
     * @param element element to insert
     */
    public void insert(E element) {
        if (!isLeaf()) {
            insertElementInChildNodes(element);
            return;
        }
        elements.add(element);

        if (depth + 1 > DEFAULT_MAX_DEPTH || elements.size() < DEFAULT_DENSITY_FACTOR)
            return;

        subdivide();
        for (E e : elements)
            insertElementInChildNodes(e);

        elements.clear();
    }

    /**
     * Removes a provided element from octree entirely
     * @param element element to remove
     */
    public void remove(E element) {
        // search for this specific element in a whole tree using stack, probably
        // ...

        // remove found element from found node(s), and if there is no elements in that node(s),
        // then rebuild subtree from bottom to top (also note, that found element can be
        // found in a completely different nodes/subtrees, so we have to keep in mind
        // that we can found way more nodes than just a single one)
        // ...

        throw new UnsupportedOperationException("Not implemented yet :(");
    }

    /**
     * Updates the position of element {@code elementA} to a new position of {@code elementB}
     * @param elementA element old position/bounds
     * @param elementB element new position/bounds
     */
    public void update(E elementA, E elementB) {
        // I think, we should use a remove() method and then we can use an insert(),
        // but we need to think more about performance costs of this approach
        // ...

        throw new UnsupportedOperationException("Not implemented yet :(");
    }

    /**
     * Checks if this node have no children nodes
     * @return {@code true} if does not contains any children nodes, {@code false} - otherwise
     */
    public boolean isLeaf() {
        return childNodes == null;
    }

    /**
     * Checks if this node has no parent node
     * @return {@code true} if has no reference to parent node, {@code false} - otherwise
     */
    public boolean isRoot() {
        return parentNode == null;
    }

    /**
     * Query all elements that lies in a provided {@code queryBox}
     * @param queryBox bounding box that represents a query for selecting elements
     * @return an iterable data structure
     */
    public Iterable<E> query(AxisAlignedBoundingBox queryBox) {
        if (!queryBox.isIntersects(this))
            return Collections.emptyList();

        return () -> new OctreeLazyIterator(queryBox);
    }

    private class OctreeLazyIterator implements Iterator<E> {
        private AxisAlignedBoundingBox queryBox;
        private OctreeNode<E> currentNode;
        private Deque<Integer> nodeIndexStack;
        private Deque<Integer> elementIndexStack;
        private Set<E> traversedElementsSet;
        private E nextElement;

        private OctreeLazyIterator(AxisAlignedBoundingBox bounds) {
            queryBox = bounds;
            nodeIndexStack = new ArrayDeque<>(maxDepth);
            elementIndexStack = new ArrayDeque<>(maxDepth);
            traversedElementsSet = new HashSet<>();

            // iterate to the deepest node of a tree that intersects with a query,
            // so we can start iterating from bottom to top
            currentNode = OctreeNode.this;
            while (!currentNode.isLeaf()) {
                OctreeNode<E>[] nodes = currentNode.childNodes;
                for (int i = 0, nodesLength = nodes.length; i < nodesLength; i++) {
                    OctreeNode<E> node = nodes[i];
                    if (queryBox.isIntersects(node)) {
                        currentNode = node;
                        nodeIndexStack.push(i);
                        elementIndexStack.push(0);
                        break;
                    }
                }
            }

            int elementIndex = 0;
            for (int elementsCount = currentNode.elements.size(); elementIndex < elementsCount; elementIndex++) {
                E e = currentNode.elements.get(elementIndex);
                if (queryBox.isIntersects(e)) {
                    // when we found first element in node which intersects with query box we break the iterations,
                    // the next elements of node will be retrieved by calling next() method of iterator
                    nextElement = e;
                    traversedElementsSet.add(e);
                    break;
                }
            }

            if (elementIndex + 1 == currentNode.elements.size())
                elementIndexStack.pop();
        }

        @Override
        public boolean hasNext() {
            return nextElement != null;
        }

        @Override
        public E next() {
            throw new UnsupportedOperationException("Not implemented yet :(");

/*
            if (!hasNext())
                throw new NoSuchElementException();

            E previouslyFoundElement = nextElement;
            E nextElementCandidate = null;
            do {
            } while (queryBox.isIntersects(nextElementCandidate));

            nextElement = nextElementCandidate;
            return previouslyFoundElement;
*/
        }
    }

    private void subdivide() {
        float xMid = (xMax + xMin) * 0.5f;
        float yMid = (yMax + yMin) * 0.5f;
        float zMid = (zMax + zMin) * 0.5f;

        childNodes = new OctreeNode[8];
        for (int i = 0; i < 8; i++) {
            int xb = (X_BIT & i);
            int yb = (Y_BIT & i) >> 1;
            int zb = (Z_BIT & i) >> 2;
            childNodes[i] = new OctreeNode<>(
                    xMin * (1 ^ xb) + xMid * xb, xMid * (1 ^ xb) + xMax * xb,
                    yMin * (1 ^ yb) + yMid * yb, yMid * (1 ^ yb) + yMax * yb,
                    zMin * (1 ^ zb) + zMid * zb, zMid * (1 ^ zb) + zMax * zb,
                    this,
                    (byte) (depth + 1)
            );
        }
    }

    private void insertElementInChildNodes(E element) {
        for (OctreeNode<E> node : childNodes) {
            if (element.isIntersects(node)) {
                node.insert(element);
                if (element.isInside(node)) break;
            }
        }
    }

    private void setBounds(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    @Override public float xMin() { return xMin; }
    @Override public float xMax() { return xMax; }
    @Override public float yMin() { return yMin; }
    @Override public float yMax() { return yMax; }
    @Override public float zMin() { return zMin; }
    @Override public float zMax() { return zMax; }
}
