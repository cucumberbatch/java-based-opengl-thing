package ecs.components;

import vectors.Vector2f;

public class Button extends AbstractECSComponent {
    public Vector2f topLeft = new Vector2f(380.f, 320.f);
    public Vector2f bottomRight = new Vector2f(440.f, 480.f);
}
