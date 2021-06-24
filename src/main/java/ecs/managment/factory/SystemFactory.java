package ecs.managment.factory;

import ecs.systems.CameraSystem;
import ecs.systems.*;

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

            case PLANE:
                return new PlaneRendererSystem();

            default:
                throw new IllegalArgumentException("No such component type name or system initialization!");
        }
    }
}
