package org.north.core.management.data;

import org.joml.Vector3f;

import java.util.Objects;

class AABB implements AxisAlignedBoundingBox {

    private Vector3f nearBottomLeft;
    private Vector3f farTopRight;

    public AABB(Vector3f nearBottomLeft, Vector3f farTopRight) {
        this.nearBottomLeft = nearBottomLeft;
        this.farTopRight = farTopRight;
    }

    @Override
    public float xMin() {
        return nearBottomLeft.x;
    }

    @Override
    public float xMax() {
        return farTopRight.x;
    }

    @Override
    public float yMin() {
        return nearBottomLeft.y;
    }

    @Override
    public float yMax() {
        return farTopRight.y;
    }

    @Override
    public float zMin() {
        return nearBottomLeft.z;
    }

    @Override
    public float zMax() {
        return farTopRight.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AABB other = (AABB) obj;
        return Float.compare(other.xMin(), xMin()) == 0 &&
                Float.compare(other.xMax(), xMax()) == 0 &&
                Float.compare(other.yMin(), yMin()) == 0 &&
                Float.compare(other.yMax(), yMax()) == 0 &&
                Float.compare(other.zMin(), zMin()) == 0 &&
                Float.compare(other.zMax(), zMax()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xMin(), xMax(), yMin(), yMax(), zMin(), zMax());
    }

    @Override
    public String toString() {
        return "AABB{" +
                "nearBottomLeft=" + nearBottomLeft +
                ", farTopRight=" + farTopRight +
                '}';
    }
}
