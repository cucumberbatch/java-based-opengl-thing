package ecs.components;

import org.joml.Vector3f;

public class RigidBody extends AbstractComponent {

    public float mass = 1.0f;

    public Vector3f centerOfMass        = new Vector3f().zero();

    public Vector3f velocity            = new Vector3f().zero();
    public Vector3f acceleration        = new Vector3f().zero();

    public Vector3f angularVelocity     = new Vector3f().zero();
    public Vector3f angularAcceleration = new Vector3f().zero();

    public boolean isGravitational      = true;

    public float frictionFactor         = 0.0f;

    @Override
    public void reset() {
        super.reset();
        mass = 1.0f;
        isGravitational = true;
        frictionFactor = 0.0f;
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
