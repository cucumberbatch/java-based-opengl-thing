package org.north.core.systems;

import org.north.core.components.RigidBody2d;
import org.north.core.components.Transform;
import org.north.core.physics.Physics;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.UpdateProcess;
import org.joml.Vector2f;

@ComponentHandler(RigidBody2d.class)
public class RigidBody2dSystem extends AbstractSystem<RigidBody2d>
        implements UpdateProcess {

    @Override
    public void update(float deltaTime) {
        if (!component.isActive()) return;

        RigidBody2d rigidBody = component;
        Transform transform = rigidBody.getTransform();

        component.velocity.add(component.acceleration.x * deltaTime, component.acceleration.y * deltaTime);
        component.angularVelocity.add(component.angularAcceleration.x * deltaTime, component.angularAcceleration.y * deltaTime);

        transform.moveRel(component.velocity.x * deltaTime, component.velocity.y * deltaTime, 0.0f);
        transform.rotation.add(component.angularVelocity.x * deltaTime, component.angularVelocity.y * deltaTime, 0.0f);

        if (rigidBody.isGravitational) {
            rigidBody.velocity.set(new Vector2f(Physics.gravityVector.x, Physics.gravityVector.y).mul(deltaTime));
        }
    }

}
