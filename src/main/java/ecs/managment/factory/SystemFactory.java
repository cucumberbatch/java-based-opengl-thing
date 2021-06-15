package ecs.managment.factory;

import ecs.math.CameraSystem;
import ecs.systems.ECSSystem;
import ecs.systems.RendererSystem;
import ecs.systems.RigidBodySystem;
import ecs.systems.TransformSystem;

@SuppressWarnings("rawtypes")
public class SystemFactory implements ComponentSystemFactory<ECSSystem> {

    @Override
    public ECSSystem create(ECSSystem.Type type) {
        switch (type) {
            case RENDERER:
                return new RendererSystem();

            case TRANSFORM:
                return new TransformSystem();

            case RIGIDBODY:
                return new RigidBodySystem();

            case CAMERA:
                return new CameraSystem();

            default:
                throw new IllegalArgumentException("No such component type name or system initialization!");
        }
    }
}
