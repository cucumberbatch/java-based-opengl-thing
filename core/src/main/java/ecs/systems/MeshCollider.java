package ecs.systems;

import ecs.components.AbstractComponent;
import ecs.shapes.Rectangle;
import vectors.Vector2f;

public class MeshCollider extends AbstractComponent {
    public Rectangle body = new Rectangle(Vector2f.zero(), Vector2f.zero());
    public boolean isStatic = false;
}
