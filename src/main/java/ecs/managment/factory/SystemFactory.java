package ecs.managment.factory;

import ecs.systems.RendererSystem;
import ecs.systems.RigidBodySystem;
import ecs.systems.System;
import ecs.systems.TransformSystem;

@SuppressWarnings("rawtypes")
public class SystemFactory implements ComponentSystemFactory<System> {

    @Override
    public System create(System.Type type) {
        switch (type) {
            case RENDERER:
                return new RendererSystem();

            case TRANSFORM:
                return new TransformSystem();

            case RIGIDBODY:
                return new RigidBodySystem();

            default:
                return null;
        }
    }
}
