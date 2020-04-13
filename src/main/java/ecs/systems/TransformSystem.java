package ecs.systems;

import ecs.components.Transform;
import org.joml.Vector3f;

import java.io.PrintStream;

public class TransformSystem extends AbstractSystem {

    @Override
    public void update(float deltaTime) {

    }

    public Vector3f getWorldPosition() {
        Transform iterable_transform = current_component.transform();
        Vector3f world_position = new Vector3f();
        while (iterable_transform.entity().parent != null) {
            world_position.add(iterable_transform.position);
            iterable_transform = iterable_transform.entity().parent;
        }
        return world_position;
    }

    public Vector3f getWorldRotation() {
        Transform iterable_transform = current_component.transform();
        Vector3f world_rotation = new Vector3f();
        while (iterable_transform.entity().parent != null) {
            world_rotation.add(iterable_transform.rotation);
            iterable_transform = iterable_transform.entity().parent;
        }
        return world_rotation;
    }


    public Vector3f getWorldScale() {
        Transform iterable_transform = current_component.transform();
        Vector3f world_scale = new Vector3f();
        while (iterable_transform.entity().parent != null) {
            world_scale.add(iterable_transform.scale);
            iterable_transform = iterable_transform.entity().parent;
        }
        return world_scale;
    }
}
