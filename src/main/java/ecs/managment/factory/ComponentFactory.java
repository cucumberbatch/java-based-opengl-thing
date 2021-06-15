package ecs.managment.factory;

import ecs.components.ECSComponent;
import ecs.components.Renderer;
import ecs.components.RigidBody;
import ecs.components.Transform;
import ecs.math.Camera;
import ecs.systems.ECSSystem;

public class ComponentFactory implements ComponentSystemFactory<ECSComponent> {

    @Override
    public ECSComponent create(ECSSystem.Type type) {
        switch (type) {
            case RENDERER:
                return new Renderer();

            case TRANSFORM:
                return new Transform();

            case RIGIDBODY:
                return new RigidBody();

            case CAMERA:
                return new Camera();

            default:
                throw new IllegalArgumentException("No such component type name or component initialization!");
        }
    }
}
