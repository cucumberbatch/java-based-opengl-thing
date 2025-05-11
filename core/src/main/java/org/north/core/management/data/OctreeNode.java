package org.north.core.management.data;

import org.joml.Vector3f;
import org.north.core.physics.collision.Collision;

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
    private int             maxElementsCount;
    private int             depth;
    private int             maxDepth;

    public OctreeNode(Vector3f nearBottomLeftPoint, Vector3f farTopRightPoint) {
        this(nearBottomLeftPoint.x, farTopRightPoint.x, nearBottomLeftPoint.y, farTopRightPoint.y, nearBottomLeftPoint.z, farTopRightPoint.z, null, 0, DEFAULT_MAX_DEPTH);
    }

    public OctreeNode(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        this(xMin, xMax, yMin, yMax, zMin, zMax, null, 0, DEFAULT_MAX_DEPTH);
    }

    private OctreeNode(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, OctreeNode<E> parentNode, int depth, int maxDepth) {
        setBounds(xMin, xMax, yMin, yMax, zMin, zMax);
        this.elements = new ArrayList<>(DEFAULT_DENSITY_FACTOR);
        this.parentNode = parentNode;
        this.depth = depth;
        this.maxDepth = maxDepth;
        this.maxElementsCount = DEFAULT_DENSITY_FACTOR;
    }

    /**
     * Inserts a provided element into this node or child nodes this node
     * @param element element to insert
     */
    public void insert(E element) {
        if (!isLeaf()) {
            insertInternal(element);
            return;
        }

        elements.add(element);

        if (depth == maxDepth || elements.size() < DEFAULT_DENSITY_FACTOR)
            return;

        if (isLeaf())
            subdivide();

        for (E e : elements)
            insertInternal(e);

        elements.clear();
    }

    private void insertInternal(E element) {
        for (OctreeNode<E> node : childNodes) {
            if (element.isIntersects(node)) {
                node.insert(element);
                if (element.isInside(node)) break;
            }
        }
    }

    private void subdivide() {
        float xMid = (xMax + xMin) * 0.5f;
        float yMid = (yMax + yMin) * 0.5f;
        float zMid = (zMax + zMin) * 0.5f;
        int increasedDepth = depth + 1;

        childNodes = new OctreeNode[]{
            new OctreeNode<E>(xMin, xMid, yMin, yMid, zMin, zMid, this,
                    increasedDepth, maxDepth),
            new OctreeNode<E>(xMid, xMax, yMin, yMid, zMin, zMid, this,
                    increasedDepth, maxDepth),
            new OctreeNode<E>(xMin, xMid, yMid, yMax, zMin, zMid, this,
                    increasedDepth, maxDepth),
            new OctreeNode<E>(xMid, xMax, yMid, yMax, zMin, zMid, this,
                    increasedDepth, maxDepth),
            new OctreeNode<E>(xMin, xMid, yMin, yMid, zMid, zMax, this,
                    increasedDepth, maxDepth),
            new OctreeNode<E>(xMid, xMax, yMin, yMid, zMid, zMax, this,
                    increasedDepth, maxDepth),
            new OctreeNode<E>(xMin, xMid, yMid, yMax, zMid, zMax, this,
                    increasedDepth, maxDepth),
            new OctreeNode<E>(xMid, xMax, yMid, yMax, zMid, zMax, this,
                    increasedDepth, maxDepth),
        };
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
        private OctreeNode<E>          currentNode;
        private Deque<Integer>         nodeIndexStack;
        private Deque<Integer>         elementIndexStack;
        private Set<E>                 traversedElementsSet;
        private E                      nextElement;

        private OctreeLazyIterator(AxisAlignedBoundingBox bounds) {
            queryBox             = bounds;
            currentNode          = OctreeNode.this;
            nodeIndexStack       = new ArrayDeque<>(maxDepth + 1);
            elementIndexStack    = new ArrayDeque<>(maxDepth + 1);
            traversedElementsSet = new HashSet<>();

            elementIndexStack.push(0);
            nodeIndexStack.push(0);

            nextElement = findNextElement();
        }

        @Override
        public boolean hasNext() {
            return nextElement != null;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();

            E element = nextElement;
            nextElement = findNextElement();
            return element;
        }

        private E findNextElement() {
            while (true) {
                // search for elements that intersects
                int elementsListSize = currentNode.elements.size();
                for (int elementIndex = elementIndexStack.pop(); elementIndex < elementsListSize; elementIndex++) {
                    E e = currentNode.elements.get(elementIndex);
                    if (queryBox.isIntersects(e) && !traversedElementsSet.contains(e)) {
                        nextElement = e;
                        elementIndexStack.push(elementIndex + 1);
                        traversedElementsSet.add(e);
                        return e;
                    }
                }
                elementIndexStack.push(maxElementsCount);

                // we need to pull out pushed indexes that relates on this node
                if (!currentNode.isLeaf()) {
                    // search for child nodes of this node
                    boolean nodeFound = false;
                    for (int nodeIndex = nodeIndexStack.pop(); nodeIndex < 8; nodeIndex++) {
                        OctreeNode<E> node = currentNode.childNodes[nodeIndex];
                        if (queryBox.isIntersects(node)) {
                            currentNode = node;
                            nodeIndexStack.push(nodeIndex + 1);
                            nodeIndexStack.push(0);
                            elementIndexStack.push(0);
                            nodeFound = true;
                            break;
                        }
                    }
                    if (nodeFound) {
                        continue;
                    } else {
                        nodeIndexStack.push(8);
                    }
                }

                if (currentNode.isRoot()) {
                    nextElement = null;
                    return null;
                } else {
                    currentNode = currentNode.parentNode;
                    nodeIndexStack.pop();
                    elementIndexStack.pop();
                }
            }
        }
    }

    public Collection<Collision> getAllCollisions() {
        Collection<Collision> collisions = new ArrayList<>();

        // Retrieve all elements from octree
        Set<E> collectedElementsForColliusionCheck = new HashSet<>();
        collectElements(this, new HashSet<>());

        // Check collisions for all given elements from octree by querying each from collection
        for (E element : collectedElementsForColliusionCheck)
            for (E collidedElement : query(element))
                collisions.add(new Collision(element, collidedElement, null));

        return collisions;
    }

    private void collectElements(OctreeNode<E> node, Set<E> elementsSet) {
        elementsSet.addAll(node.elements);

        if (node.isLeaf())
            return;

        for (OctreeNode<E> childNode : node.childNodes)
            collectElements(childNode, elementsSet);
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

    @Override
    public String toString() {
        return printRecursive(this, 0, new StringBuilder("Root")).toString();
    }

    private StringBuilder printRecursive(OctreeNode<E> node, int depth,
                                     StringBuilder buffer) {
        if (node == null) return buffer;

        // Создаем отступ
        for (int i = 0; i < depth; i++) {
            buffer.append("  ");
        }

        // Выводим информацию о текущем узле
        buffer.append(": ").append((node.isLeaf() ? "Leaf\n" : "Internal\n"));

        // Рекурсивно выводим детей
        if (!node.isLeaf()) {
            for (int i = 0; i < 8; i++) {
                buffer.append(printRecursive(node.childNodes[i], depth + 1,
                        buffer.append("Child ").append(i)));
            }
        }

        return buffer;
    }
}
