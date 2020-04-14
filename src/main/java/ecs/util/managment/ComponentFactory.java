package ecs.util.managment;

import ecs.components.Component;
import ecs.components.RigidBody;
import ecs.components.Transform;
import ecs.systems.System;

public class ComponentFactory implements Factory<Component> {

    @Override
    public Component create(System.Type type) {
        switch (type) {
            case TRANSFORM:
                return new Transform();

            case RIGIDBODY:
                return new RigidBody();

            default:
                return null;
        }
    }
}
