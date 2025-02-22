package org.north.core.management.data;

public interface AxisAlignedBoundingBox {
    float xMin();
    float xMax();
    float yMin();
    float yMax();
    float zMin();
    float zMax();

    default boolean isIntersects(AxisAlignedBoundingBox aabb) {
        return xMin() <= aabb.xMax() && xMax() >= aabb.xMin()
            && yMin() <= aabb.yMax() && yMax() >= aabb.yMin()
            && zMin() <= aabb.zMax() && zMax() >= aabb.zMin();
    }

    default boolean isInside(AxisAlignedBoundingBox aabb) {
        return xMin() >= aabb.xMin() && xMax() <= aabb.xMax()
            && yMin() >= aabb.yMin() && yMax() <= aabb.yMax()
            && zMin() >= aabb.zMin() && zMax() <= aabb.zMax();
    }
}
