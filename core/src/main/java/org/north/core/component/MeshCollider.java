package org.north.core.component;

import org.north.core.shape.Rectangle;
import org.joml.Vector2f;

public class MeshCollider extends AbstractComponent {
    public Rectangle body = new Rectangle(new Vector2f().zero(), new Vector2f().zero());
    public boolean isStatic = false;

}
