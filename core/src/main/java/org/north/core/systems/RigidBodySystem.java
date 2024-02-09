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
        RigidBody rigidBody = this.getComponent();
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
        rigidBody.velocity.add(Physics.gravityVector.mul(deltaTime, temp));
    }

    private void updateAngularVelocity(RigidBody that, float deltaTime, Vector3f temp) {
        that.angularVelocity.add(that.angularAcceleration.mul(deltaTime * deltaTime, temp));
    }

    private void updateVelocity(RigidBody that, float deltaTime, Vector3f temp) {
        that.velocity.add(that.acceleration.mul(deltaTime * deltaTime, temp));
    }

    private void updateRotation(Transform transform, RigidBody that, float deltaTime, Vector3f temp) {
        transform.rotation.add(that.angularVelocity.mul(deltaTime, temp));
    }

    private void updatePosition(Transform transform, RigidBody that, float deltaTime, Vector3f temp) {
        transform.moveRel(that.velocity.mul(deltaTime, temp));
    }

    public void addImpulseToMassCenter(Vector3f direction, float mass) {
        Vector3f temp = vector3IPool.get();
        component.velocity.add(direction.mul(mass, temp));
        vector3IPool.put(temp);
    }

}
