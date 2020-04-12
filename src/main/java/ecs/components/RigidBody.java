package ecs.components;

import org.joml.Vector3f;

public class RigidBody extends Component {
    public float mass;
    public Vector3f centerOfMass;

    public Vector3f velocity;
    public Vector3f acceleration;

    public Vector3f angularVelocity;
    public Vector3f angularAcceleration;

    public boolean isGravitational;


    @Override
    public RigidBody getInstance() {
        return new RigidBody();
    }
}
