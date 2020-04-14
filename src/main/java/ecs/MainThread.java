package ecs;

import ecs.components.RigidBody;
import ecs.entities.Entity;
import ecs.systems.System;
import org.joml.Vector3f;

public class MainThread {
    public static void main(String[] args) throws InterruptedException {
        Scene scene = new Scene();
        Engine engine = new Engine(scene);
        Entity entity = new Entity(engine, scene);

        entity.AddComponent(System.Type.RIGIDBODY);

        RigidBody rigidBody = entity.GetComponent(System.Type.RIGIDBODY);

        int count = 0;
        long rest = 100;

        while (count != 10000) {
            engine.tick(0.0001f);
            java.lang.System.out.println("position:\t" + entity.transform.position + "\t\tvelocity:\t" + ((RigidBody) entity.GetComponent(System.Type.RIGIDBODY)).velocity + "\t\tacceleration:\t" + ((RigidBody) entity.GetComponent(System.Type.RIGIDBODY)).acceleration);
            count++;
            Thread.sleep(rest);
        }

    }
}
