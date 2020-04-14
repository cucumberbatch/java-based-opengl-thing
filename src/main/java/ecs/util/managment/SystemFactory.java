package ecs.util.managment;

import ecs.systems.RigidBodySystem;
import ecs.systems.System;
import ecs.systems.TransformSystem;

public class SystemFactory implements Factory<System> {

    @Override
    public System create(System.Type type) {
        switch (type) {
            case TRANSFORM:
                return new TransformSystem();

            case RIGIDBODY:
                return new RigidBodySystem();

            default:
                return null;
        }
    }
}
