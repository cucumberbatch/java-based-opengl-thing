package ecs.physics;

import ecs.components.RigidBody;
import ecs.components.Transform;
import ecs.managment.ObjectsStorage;
import ecs.math.Vector3f;

public class Physics {
    public static float gravityFactor = 9.81f;
    public static Vector3f gravityVector = Vector3f.down().mul(gravityFactor);


    public static void updateGravitationalAcceleration(RigidBody body, float deltaTime, Vector3f temp) {
        temp.set(Physics.gravityVector);
        body.velocity.add(temp.mul(deltaTime));
    }

    public static void updateAngularVelocity(RigidBody body, float deltaTime, Vector3f temp) {
        temp.set(body.angularAcceleration);
        body.angularVelocity.add(temp.mul(deltaTime * deltaTime));
    }

    public static void updateVelocity(RigidBody body, float deltaTime, Vector3f temp) {
        temp.set(body.acceleration);
        body.velocity.add(temp.mul(deltaTime * deltaTime));
    }

    public static void updateRotation(Transform transform, RigidBody body, float deltaTime, Vector3f temp) {
        temp.set(body.angularVelocity);
        transform.rotation.add(temp.mul(deltaTime));
    }

    public static void updatePosition(Transform transform, RigidBody body, float deltaTime, Vector3f temp) {
        temp.set(body.velocity);
        transform.position.add(temp.mul(deltaTime));
    }

    public static void addImpulseToMassCenter(Vector3f direction, RigidBody body, float mass, Vector3f temp) {
        temp.set(direction);
        body.velocity.add(temp.mul(mass));
    }

    public void addImpulseToMassCenter(Vector3f direction, RigidBody body,  float mass) {
        Vector3f temp = ObjectsStorage.vector3fPool.get().set(direction);
        addImpulseToMassCenter(direction, body, mass, temp);
        ObjectsStorage.vector3fPool.put(temp);
    }

}
