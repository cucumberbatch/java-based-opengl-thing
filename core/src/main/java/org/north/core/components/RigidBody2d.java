package org.north.core.components;

import org.joml.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RigidBody2d extends AbstractComponent {

    public float    mass                = 1.0f;

    public Vector2f centerOfMass        = new Vector2f().zero();

    public Vector2f previousPosition    = new Vector2f().zero();
    public Vector2f currentPosition     = new Vector2f().zero();

    public Vector2f velocity            = new Vector2f().zero();
    public Vector2f acceleration        = new Vector2f().zero();

    public Vector2f angularVelocity     = new Vector2f().zero();
    public Vector2f angularAcceleration = new Vector2f().zero();

    public float    frictionFactor      = 0.0f;

    public boolean  isGravitational     = true;

    // Hooke's law
    public Vector2f restoringForce      = new Vector2f().zero();
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

    @Override
    protected void serialize(ObjectOutputStream out) throws IOException {

    }

    @Override
    protected void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {

    }
}
