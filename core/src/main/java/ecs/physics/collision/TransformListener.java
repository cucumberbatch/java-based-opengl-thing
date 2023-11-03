package ecs.physics.collision;

import ecs.entities.Entity;
import org.joml.Vector3f;

public interface TransformListener {
    void registerMovement(Entity entity, Vector3f previousPosition, Vector3f currentPosition);
}
