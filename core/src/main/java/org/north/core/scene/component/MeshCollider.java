package org.north.core.scene.component;

import org.north.core.physics.collision.CollisionLayer;
import org.north.core.physics.shape.Rectangle;
import org.north.core.physics.spatial.Aabb;
import org.joml.Vector2f;

import java.util.EnumSet;
import java.util.Set;

public class MeshCollider extends AbstractComponent {

    public enum Space {
        /** The collider lives in window pixels and is used for UI interaction. */
        SCREEN,
        /** The collider lives in world units and is used for scene picking. */
        WORLD
    }

    public Space space = Space.SCREEN;

    /** Screen-space rectangle. Used when {@link #space} is {@link Space#SCREEN}. */
    public Rectangle body = new Rectangle(new Vector2f().zero(), new Vector2f().zero());

    /**
     * Bounds of the collider in local mesh space. The default matches the engine's unit-sized
     * predefined meshes (a cube or quad from -0.5 to +0.5).
     */
    public Aabb localBounds = new Aabb(-0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f);

    /** World-space bounds. Updated by {@code MeshColliderSystem}. */
    public final Aabb bounds = new Aabb();

    /** World colliders with this flag disabled are skipped by scene picking. */
    public boolean selectable = true;

    public Set<CollisionLayer> layer = EnumSet.of(CollisionLayer.DEFAULT);
    public boolean isStatic = false;

}
