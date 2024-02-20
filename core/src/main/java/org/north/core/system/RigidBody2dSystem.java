package org.north.core.system;

import org.north.core.component.RigidBody2d;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.physics.Physics;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.UpdateProcess;

@ComponentHandler(RigidBody2d.class)
public class RigidBody2dSystem extends AbstractSystem<RigidBody2d>
        implements UpdateProcess<RigidBody2d> {

    @Inject
    public RigidBody2dSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void update(RigidBody2d rigidBody, float deltaTime) {
        Transform transform = rigidBody.getTransform();

        rigidBody.velocity.add(rigidBody.acceleration.x * deltaTime, rigidBody.acceleration.y * deltaTime);
        rigidBody.angularVelocity.add(rigidBody.angularAcceleration.x * deltaTime, rigidBody.angularAcceleration.y * deltaTime);

        transform.moveRel(rigidBody.velocity.x * deltaTime, rigidBody.velocity.y * deltaTime, 0.0f);
        transform.rotation.add(rigidBody.angularVelocity.x * deltaTime, rigidBody.angularVelocity.y * deltaTime, 0.0f);

        if (rigidBody.isGravitational) {
            rigidBody.velocity.set(Physics.gravityVector.x * deltaTime, Physics.gravityVector.y * deltaTime);
        }
    }

}
