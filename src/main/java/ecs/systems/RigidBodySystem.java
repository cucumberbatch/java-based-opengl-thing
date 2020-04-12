package ecs.systems;

import ecs.components.RigidBody;
import ecs.components.Transform;

public class RigidBodySystem extends System {

    @Override
    public void update(float deltaTime) {
        RigidBody that = (RigidBody) current_component;
        Transform transform = that.transform;

        transform.position.add(that.velocity.mul(deltaTime));

//        transform.position.add(that.velocity.mul(deltaTime));
//        that.velocity.add(that.acceleration.mul(deltaTime * deltaTime));
//
//        transform.rotation.add(that.angularVelocity.mul(deltaTime));
//        that.angularVelocity.add(that.angularAcceleration.mul(deltaTime * deltaTime));
    }
}
