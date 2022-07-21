package ecs.systems;

import ecs.components.RigidBody2d;
import ecs.components.Transform;
import ecs.physics.Physics;
import vectors.Vector2f;

public class RigidBody2dSystem extends AbstractECSSystem<RigidBody2d> {

    @Override
    public void update(float deltaTime) {
        if (!component.isActive()) return;

        RigidBody2d rigidBody = component;
        Transform transform = rigidBody.getTransform();

        component.velocity.add(component.acceleration.x * deltaTime, component.acceleration.y * deltaTime);
        component.angularVelocity.add(component.angularAcceleration.x * deltaTime, component.angularAcceleration.y * deltaTime);
        transform.position.add(component.velocity.x * deltaTime, component.velocity.y * deltaTime, 0.0f);
        transform.rotation.add(component.angularVelocity.x * deltaTime, component.angularVelocity.y * deltaTime, 0.0f);

        if (rigidBody.isGravitational) {
            rigidBody.velocity.set(new Vector2f(Physics.gravityVector.x, Physics.gravityVector.y).mul(deltaTime));
        }
    }

}
