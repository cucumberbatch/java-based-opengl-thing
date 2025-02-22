package org.north.core.management.data;

import org.joml.Vector3f;

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
}
