package org.north.core.components;

public class MovementComponent extends AbstractComponent implements Cloneable {
    public float range = 1f;
    public float acc = 0f;
    public float initialZ;

    @Override
    public MovementComponent clone() {
        MovementComponent clone = (MovementComponent) super.clone();
        clone.range = this.range;
        clone.acc = this.acc;
        clone.initialZ = this.initialZ;
        return clone;
    }
}
