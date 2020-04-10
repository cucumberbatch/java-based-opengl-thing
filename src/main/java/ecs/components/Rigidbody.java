package ecs.components;

import ecs.math.Vector3;

public class Rigidbody extends Component {
    public float mass;
    public Vector3 centerOfMass;

    public Vector3 velocity;
    public Vector3 acceleration;

    public Vector3 angularVelocity;
    public Vector3 angularAcceleration;

    public boolean isGravitational;

}
