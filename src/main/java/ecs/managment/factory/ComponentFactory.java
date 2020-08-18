package ecs.managment.factory;

import ecs.components.Component;
import ecs.components.Renderer;
import ecs.components.RigidBody;
import ecs.components.Transform;
import ecs.systems.System;

public class ComponentFactory implements ComponentSystemFactory<Component> {

    @Override
    public Component create(System.Type type) {
        switch (type) {
            case RENDERER:
                return new Renderer();

            case TRANSFORM:
                return new Transform();

            case RIGIDBODY:
                return new RigidBody();

            default:
                return null;
        }
    }
}
