package ecs.systems;

import ecs.components.AbstractECSComponent;
import ecs.shapes.Rectangle;
import vectors.Vector2f;

public class MeshCollider extends AbstractECSComponent {
    public Rectangle mesh = new Rectangle(Vector2f.zero(), Vector2f.zero());

}
