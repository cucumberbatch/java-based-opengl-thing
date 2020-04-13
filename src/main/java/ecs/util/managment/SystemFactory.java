package ecs.util.managment;

import ecs.components.ComponentType;
import ecs.systems.RigidBodySystem;
import ecs.systems.System;
import ecs.systems.TransformSystem;

public class SystemFactory implements Factory<System> {

    @Override
    public System create(ComponentType type) {
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
