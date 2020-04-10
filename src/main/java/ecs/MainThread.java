package ecs;

import ecs.components.Rigidbody;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.math.Vector3;

public class MainThread {
    public static void main(String[] args) {
        Scene firstScene = new Scene();
        Engine engine = new Engine(firstScene);

        Entity gameObject = new Entity(engine, firstScene);

        engine.helper.linkComponentAndSystem(gameObject.transform);

        System.out.print(gameObject.transform.system.getClass().getSimpleName() + "\n");

        gameObject.AddComponent(Rigidbody::new);

        engine.helper.linkComponentAndSystem(gameObject.GetComponent(Rigidbody::new));

        System.out.print(gameObject.GetComponent(Rigidbody::new).system.getClass().getSimpleName());


//
//        gameObject.AddComponent(Rigidbody::new);
//        System.out.print(gameObject.GetComponent(Rigidbody::new));
//
//        System.out.print(gameObject.engine.helper.getSystemByComponentName("Transform"));

    }
}
