package ecs.util.managment;

import ecs.components.Component;
import ecs.components.ComponentType;
import ecs.components.RigidBody;
import ecs.components.Transform;

public class ComponentFactory implements Factory<Component> {

    @Override
    public Component create(ComponentType type) {
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
