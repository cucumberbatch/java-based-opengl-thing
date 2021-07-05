package ecs.systems;

import ecs.components.RigidBody;
import ecs.components.Transform;
import ecs.managment.ObjectsStorage;
import ecs.math.Vector3f;

import static ecs.physics.Physics.*;

public class RigidBodySystem extends AbstractECSSystem<RigidBody> {

    public RigidBodySystem() {
        setWorkflowMask(UPDATE_MASK);
    }

    @Override
    public void onUpdate(float deltaTime) {
        if (!component.isActive()) return;

        RigidBody rigidBody = component;
        Transform transform = rigidBody.getTransform();

        /* Catch the free vector from pool for calculations */
        Vector3f temp = ObjectsStorage.vector3fPool.get();

        /* Update all kinds of movement for transform and rigid body components */
        updatePosition(transform, rigidBody, deltaTime, temp);
        updateRotation(transform, rigidBody, deltaTime, temp);
        updateVelocity(rigidBody, deltaTime, temp);
        updateAngularVelocity(rigidBody, deltaTime, temp);

        /* Apply gravitational force to an object */
        if (rigidBody.isGravitational) {
            updateGravitationalAcceleration(rigidBody, deltaTime, temp);
        }

        /* Take it back */
        ObjectsStorage.vector3fPool.put(temp);
    }

}
