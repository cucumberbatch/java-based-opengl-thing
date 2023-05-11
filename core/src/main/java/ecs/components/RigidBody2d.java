package ecs.components;

import vectors.Vector2f;

public class RigidBody2d extends AbstractComponent {

    public float    mass                = 1.0f;

    public Vector2f centerOfMass        = Vector2f.zero();

    public Vector2f previousPosition    = Vector2f.zero();
    public Vector2f currentPosition     = Vector2f.zero();

    public Vector2f velocity            = Vector2f.zero();
    public Vector2f acceleration        = Vector2f.zero();

    public Vector2f angularVelocity     = Vector2f.zero();
    public Vector2f angularAcceleration = Vector2f.zero();

    public float    frictionFactor      = 0.0f;

    public boolean  isGravitational     = true;

    // Hooke's law
    public Vector2f restoringForce      = Vector2f.zero();
    public float    springFactor        = 0.0f;


    @Override
    public String toString() {
        return  "\nmass:                " + mass +
                "\ncenterOfMass:        " + centerOfMass +
                "\npreviousPosition:    " + previousPosition +
                "\ncurrentPosition:     " + currentPosition +
                "\nvelocity:            " + velocity +
                "\nacceleration:        " + acceleration +
                "\nangularVelocity:     " + angularVelocity +
                "\nangularAcceleration: " + angularAcceleration +
                "\nisGravitational:     " + isGravitational +
                "\nfrictionFactor:      " + frictionFactor +
                "\nrestoringForce:      " + restoringForce +
                "\nspringFactor:        " + springFactor;
    }
}
