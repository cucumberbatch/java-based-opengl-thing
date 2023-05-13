package ecs.systems;

import ecs.components.AbstractComponent;
import ecs.shapes.Rectangle;
import vectors.Vector2f;

public class MeshCollider extends AbstractComponent {
    public Rectangle mesh = new Rectangle(Vector2f.zero(), Vector2f.zero());
}