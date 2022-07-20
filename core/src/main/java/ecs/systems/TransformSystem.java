package ecs.systems;

import ecs.components.Transform;
import vectors.Vector3f;

public class TransformSystem extends AbstractECSSystem<Transform> {

    public Vector3f getWorldPosition() {
        Transform iterableTransform = component.getTransform();
        Vector3f worldPosition = new Vector3f();
        while (iterableTransform.parent != null) {
            worldPosition.add(iterableTransform.position);
            iterableTransform = iterableTransform.parent;
        }
        return worldPosition;
    }

    public Vector3f getWorldRotation() {
        Transform iterableTransform = component.getTransform();
        Vector3f worldRotation = new Vector3f();
        while (iterableTransform.parent != null) {
            worldRotation.add(iterableTransform.rotation);
            iterableTransform = iterableTransform.parent;
        }
        return worldRotation;
    }

    public Vector3f getWorldScale() {
        Transform iterableTransform = component.getTransform();
        Vector3f worldScale = new Vector3f();
        while (iterableTransform.parent != null) {
            worldScale.add(iterableTransform.scale);
            iterableTransform = iterableTransform.parent;
        }
        return worldScale;
    }

    public Transform getWorldTransform() {
        Transform iterableTransform = component.getTransform();
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
