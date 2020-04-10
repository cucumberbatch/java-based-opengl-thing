package ecs;

import ecs.components.Component;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.util.Vector3;

public class MainThread {
    public static void main(String[] args) {
        Entity entity = new Entity();

        entity.AddComponent(Transform::new);
        System.out.print(entity.GetComponent(Transform::new));
        entity.GetComponent(Transform::new).position = Vector3.one();
        System.out.print(entity.GetComponent(Transform::new));

//
//        System.out.print(entity.GetComponent(Transform.class).position.x);
//
//        entity.GetComponent(Transform.class).position = new Vector3(1, 0, 0);

//        System.out.print(entity.GetComponent(Transform.class).position.x);
    }
}
