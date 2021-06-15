package ecs.systems;

import ecs.components.Transform;
import ecs.math.Vector3f;

public class TransformSystem extends AbstractECSSystem<Transform> {

    public Vector3f getWorldPosition() {
        Transform iterable_transform = currentComponent.getTransform();
        Vector3f world_position = new Vector3f();
        while (iterable_transform.parent != null) {
            world_position.add(iterable_transform.position);
            iterable_transform = iterable_transform.parent;
        }
        return world_position;
    }

    public Vector3f getWorldRotation() {
        Transform iterable_transform = currentComponent.getTransform();
        Vector3f world_rotation = new Vector3f();
        while (iterable_transform.parent != null) {
            world_rotation.add(iterable_transform.rotation);
            iterable_transform = iterable_transform.parent;
        }
        return world_rotation;
    }

    public Vector3f getWorldScale() {
        Transform iterable_transform = currentComponent.getTransform();
        Vector3f world_scale = new Vector3f();
        while (iterable_transform.parent != null) {
            world_scale.add(iterable_transform.scale);
            iterable_transform = iterable_transform.parent;
        }
        return world_scale;
    }

    public Transform getWorldTransform() {
        Transform iterable_transform = currentComponent.getTransform();
        Transform world_transform = new Transform();
        while (iterable_transform.parent != null) {
            world_transform.position.add(iterable_transform.position);
            world_transform.rotation.add(iterable_transform.rotation);
            world_transform.scale.add(iterable_transform.scale);
            iterable_transform = iterable_transform.parent;
        }
        return world_transform;
    }
}
