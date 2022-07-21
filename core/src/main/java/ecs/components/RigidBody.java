package ecs.components;

import vectors.Vector3f;

public class RigidBody extends AbstractECSComponent {

    public float mass = 1.0f;

    public Vector3f centerOfMass        = Vector3f.zero();

    public Vector3f velocity            = Vector3f.zero();
    public Vector3f acceleration        = Vector3f.zero();

    public Vector3f angularVelocity     = Vector3f.zero();
    public Vector3f angularAcceleration = Vector3f.zero();

    public boolean isGravitational      = true;

    public float frictionFactor         = 0.0f;

    @Override
    public void reset() {
        super.reset();
        mass = 1.0f;

        Vector3f.reset(
                centerOfMass,
                velocity,
                acceleration,
                angularVelocity,
                angularAcceleration
        );

        isGravitational = true;
        frictionFactor = 0.0f;
    }

    // TODO: 07.06.2021 implementation isn't done yet
    @Override
    public <E extends ECSComponent> E getReplica() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RigidBody getInstance() {
        return new RigidBody();
    }


    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String toString() {
        return  "\nmass:                " + mass +
                "\ncenterOfMass:        " + centerOfMass +
                "\nvelocity:            " + velocity +
                "\nacceleration:        " + acceleration +
                "\nangularVelocity:     " + angularVelocity +
                "\nangularAcceleration: " + angularAcceleration +
                "\nisGravitational:     " + isGravitational +
                "\nfrictionFactor:      " + frictionFactor;
    }
}