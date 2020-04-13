package ecs.util.managment.memory;

import org.joml.Vector3f;

public class Vector3Pool extends AbstractPool<Vector3f> {

    @Override
    public Vector3f create() {
        return new Vector3f();
    }

}
