package ecs.physics.collision;

import ecs.entities.Entity;
import ecs.systems.MeshCollider;
import ecs.utils.Logger;
import org.joml.Vector3f;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeshTransformListener implements TransformListener {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    @Override
    public void registerMovement(Entity entity, Vector3f previousPosition, Vector3f currentPosition) {
        if (!previousPosition.equals(currentPosition)) {
//            runAsync(entity, previousPosition, currentPosition);
        }
    }

    private void runAsync(Entity entity, Vector3f previousPosition, Vector3f currentPosition) {
        executor.execute(() -> {
            MeshCollider collider = entity.getComponent(MeshCollider.class);
            Logger.info(String.format("Registered movement of entity [%s]: %s -> %s", entity.getName(), previousPosition.toString(), currentPosition.toString()));
        });
    }

    public static void shutdownThreadExecution() {
        executor.shutdown();
    }
}
