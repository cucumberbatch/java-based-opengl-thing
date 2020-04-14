package ecs.components;

import org.joml.Vector3f;

public class RigidBody extends AbstractComponent {
    public float mass = 1.0f;
    public Vector3f centerOfMass = new Vector3f(0, 0, 0);

    public Vector3f velocity = new Vector3f(0, 0, 0);
    public Vector3f acceleration = new Vector3f(0, 0, 0);

    public Vector3f angularVelocity = new Vector3f(0, 0, 0);
    public Vector3f angularAcceleration = new Vector3f(0, 0, 0);

    public boolean isGravitational = true;

    public float frictionFactor = 0.0f;

    @Override
    public <E extends Component> E getReplica() {
        return null;
    }

    @Override
    public RigidBody getInstance() {
        return new RigidBody();
    }
}
