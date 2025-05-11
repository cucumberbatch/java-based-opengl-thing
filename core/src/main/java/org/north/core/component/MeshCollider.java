package org.north.core.component;

import org.north.core.physics.collision.CollisionLayer;
import org.north.core.shape.Rectangle;
import org.joml.Vector2f;

import java.util.EnumSet;
import java.util.Set;

public class MeshCollider extends AbstractComponent {
    public Rectangle body = new Rectangle(new Vector2f().zero(), new Vector2f().zero());
    public Set<CollisionLayer> layer = EnumSet.of(CollisionLayer.DEFAULT);
    public boolean isStatic = false;

}
