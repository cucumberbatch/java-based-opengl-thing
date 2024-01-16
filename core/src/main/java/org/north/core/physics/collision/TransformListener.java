package org.north.core.physics.collision;

import org.north.core.entities.Entity;
import org.joml.Vector3f;

public interface TransformListener {
    void registerMovement(Entity entity, Vector3f previousPosition, Vector3f currentPosition);
}
