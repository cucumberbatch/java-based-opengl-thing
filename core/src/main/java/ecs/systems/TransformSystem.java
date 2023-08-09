package ecs.systems;

import ecs.components.Transform;
import ecs.reflection.ComponentHandler;
import vectors.Vector3f;

@ComponentHandler(Transform.class)
public class TransformSystem extends AbstractSystem<Transform> {

    public static Vector3f getWorldPosition(Transform transform) {
        Transform iterableTransform = transform;
        Vector3f worldPosition = new Vector3f();
        while (iterableTransform.parent != null) {
            worldPosition.add(iterableTransform.position);
            iterableTransform = iterableTransform.parent;
        }
        return worldPosition;
    }

    public static Vector3f getWorldRotation(Transform transform) {
        Transform iterableTransform = transform;
        Vector3f worldRotation = new Vector3f();
        while (iterableTransform.parent != null) {
            worldRotation.add(iterableTransform.rotation);
            iterableTransform = iterableTransform.parent;
        }
        return worldRotation;
    }

    public static Vector3f getWorldScale(Transform transform) {
        Transform iterableTransform = transform;
        Vector3f worldScale = new Vector3f();
        while (iterableTransform.parent != null) {
            worldScale.add(iterableTransform.scale);
            iterableTransform = iterableTransform.parent;
        }
        return worldScale;
    }

    public static Transform getWorldTransform(Transform transform) {
        Transform iterableTransform = transform;
        Transform worldTransform = new Transform();
        while (iterableTransform.parent != null) {
            worldTransform.position.add(iterableTransform.position);
            worldTransform.rotation.add(iterableTransform.rotation);
            worldTransform.scale.add(iterableTransform.scale);
            iterableTransform = iterableTransform.parent;
        }
        return worldTransform;
    }
}
