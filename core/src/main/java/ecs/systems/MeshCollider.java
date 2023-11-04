package ecs.systems;

import ecs.components.AbstractComponent;
import ecs.shapes.Rectangle;
import org.joml.Vector2f;

public class MeshCollider extends AbstractComponent {
    public Rectangle body = new Rectangle(new Vector2f().zero(), new Vector2f().zero());
    public boolean isStatic = false;
}
