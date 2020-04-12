package ecs;

import ecs.components.ComponentType;
import ecs.components.RigidBody;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.util.Physics;
import org.joml.Vector3f;

public class MainThread {
    public static void main(String[] args) {
        Scene firstScene = new Scene();
        Engine engine = new Engine(firstScene);

        Entity object1 = new Entity(engine, firstScene);
        Entity object2 = new Entity(engine, firstScene);
        Entity object3 = new Entity(engine, firstScene);

        object2.attachTo(object1);
        object3.attachTo(object2);

        ((Transform) object1.GetComponent(ComponentType.TRANSFORM)).position = new Vector3f(1, 0, 0);
        ((Transform) object2.GetComponent(ComponentType.TRANSFORM)).position = new Vector3f(1, 0, 0);
        ((Transform) object3.GetComponent(ComponentType.TRANSFORM)).position = new Vector3f(1, 0, 0);

        engine.tick(1f);


    }
}
