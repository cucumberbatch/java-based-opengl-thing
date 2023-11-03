package ecs.systems;

import ecs.components.Transform;
import ecs.reflection.ComponentHandler;
import org.joml.Vector3f;

@ComponentHandler(Transform.class)
public class TransformSystem extends AbstractSystem<Transform> {

    public static Vector3f getWorldPosition(Transform transform) {
        Transform iterableTransform = transform;
        Vector3f worldPosition = new Vector3f();
        do {
            worldPosition.add(iterableTransform.position);
            iterableTransform = iterableTransform.parent;
        } while (iterableTransform.parent != null);
        return worldPosition;
    }

    public static Vector3f getWorldRotation(Transform transform) {
        Transform iterableTransform = transform;
        Vector3f worldRotation = new Vector3f();
        do {
            worldRotation.add(iterableTransform.rotation);
            iterableTransform = iterableTransform.parent;
        } while (iterableTransform.parent != null);
        return worldRotation;
    }

    public static Vector3f getWorldScale(Transform transform) {
        Transform iterableTransform = transform;
        Vector3f worldScale = new Vector3f();
        do {
            worldScale.set(
                    worldScale.x * iterableTransform.scale.x,
                    worldScale.y * iterableTransform.scale.y,
                    worldScale.z * iterableTransform.scale.z
            );
            iterableTransform = iterableTransform.parent;
        } while (iterableTransform.parent != null);
        return worldScale;
    }

    public static Transform getWorldTransform(Transform transform) {
        Transform iterableTransform = transform;
        Transform worldTransform = new Transform();
        do {
            worldTransform.position.add(iterableTransform.position);
            worldTransform.rotation.add(iterableTransform.rotation);
            worldTransform.scale.set(
                    worldTransform.scale.x * iterableTransform.scale.x,
                    worldTransform.scale.y * iterableTransform.scale.y,
                    worldTransform.scale.z * iterableTransform.scale.z
            );
            iterableTransform = iterableTransform.parent;
        } while (iterableTransform != null);
        return worldTransform;
    }
}
