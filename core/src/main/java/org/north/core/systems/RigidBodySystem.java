package org.north.core.systems;

import org.north.core.components.RigidBody;
import org.north.core.components.Transform;
import org.north.core.physics.Physics;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.UpdateProcess;
import org.joml.Vector3f;

@ComponentHandler(RigidBody.class)
public class RigidBodySystem extends AbstractSystem<RigidBody>
        implements UpdateProcess {

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
        transform.moveRel(temp.mul(deltaTime));
    }

    public void addImpulseToMassCenter(Vector3f direction, float mass) {
        Vector3f temp = vector3IPool.get().set(direction);
        component.velocity.add(temp.mul(mass));
        vector3IPool.put(temp);
    }

}
