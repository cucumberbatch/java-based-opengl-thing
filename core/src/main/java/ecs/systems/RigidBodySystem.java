package ecs.systems;

import ecs.components.RigidBody;
import ecs.components.Transform;
import ecs.physics.Physics;
import vectors.Vector3f;

public class RigidBodySystem extends AbstractECSSystem<RigidBody> {

    @Override
    public void update(float deltaTime) {
        if (!component.isActive()) return;

        RigidBody rigidBody = component;
        Transform transform = rigidBody.getTransform();

        /* Catch the free vector from pool for calculations */
        Vector3f temp = vector3IPool.get();

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
        vector3IPool.put(temp);
    }

    private void updateGravitationalAcceleration(RigidBody rigidBody, float deltaTime, Vector3f temp) {
        temp.set(Physics.gravityVector);
        rigidBody.velocity.add(temp.mul(deltaTime));
    }

    private void updateAngularVelocity(RigidBody that, float deltaTime, Vector3f temp) {
        temp.set(that.angularAcceleration);
        that.angularVelocity.add(temp.mul(deltaTime * deltaTime));
    }

    private void updateVelocity(RigidBody that, float deltaTime, Vector3f temp) {
        temp.set(that.acceleration);
        that.velocity.add(temp.mul(deltaTime * deltaTime));
    }

    private void updateRotation(Transform transform, RigidBody that, float deltaTime, Vector3f temp) {
        temp.set(that.angularVelocity);
        transform.rotation.add(temp.mul(deltaTime));
    }

    private void updatePosition(Transform transform, RigidBody that, float deltaTime, Vector3f temp) {
        temp.set(that.velocity);
        transform.position.add(temp.mul(deltaTime));
    }

    public void addImpulseToMassCenter(Vector3f direction, float mass) {
        Vector3f temp = vector3IPool.get().set(direction);
        component.velocity.add(temp.mul(mass));
        vector3IPool.put(temp);
    }

}
