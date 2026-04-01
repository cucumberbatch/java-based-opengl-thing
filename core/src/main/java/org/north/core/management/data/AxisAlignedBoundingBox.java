package org.north.core.management.data;

public interface AxisAlignedBoundingBox {
    float xMin();
    float xMax();
    float yMin();
    float yMax();
    float zMin();
    float zMax();

    default boolean isIntersects(AxisAlignedBoundingBox other) {
        return this.xMin() <= other.xMax() && this.xMax() >= other.xMin()
            && this.yMin() <= other.yMax() && this.yMax() >= other.yMin()
            && this.zMin() <= other.zMax() && this.zMax() >= other.zMin();
    }

    default boolean isInside(AxisAlignedBoundingBox other) {
        return this.xMin() >= other.xMin() && this.xMax() <= other.xMax()
            && this.yMin() >= other.yMin() && this.yMax() <= other.yMax()
            && this.zMin() >= other.zMin() && this.zMax() <= other.zMax();
    }
}
