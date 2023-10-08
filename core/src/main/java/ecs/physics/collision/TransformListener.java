package ecs.physics.collision;

import ecs.entities.Entity;
import vectors.Vector3f;

public interface TransformListener {
    void registerMovement(Entity entity, Vector3f previousPosition, Vector3f currentPosition);
}
