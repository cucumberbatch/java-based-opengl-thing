package org.north.core.components;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    @Override
    protected void serialize(ObjectOutputStream out) throws IOException {

    }

    @Override
    protected void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {

    }
}
