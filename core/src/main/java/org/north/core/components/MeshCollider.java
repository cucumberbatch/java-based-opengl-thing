package org.north.core.components;

import org.north.core.components.AbstractComponent;
import org.north.core.shapes.Rectangle;
import org.joml.Vector2f;

public class MeshCollider extends AbstractComponent {
    public Rectangle body = new Rectangle(new Vector2f().zero(), new Vector2f().zero());
    public boolean isStatic = false;
}
