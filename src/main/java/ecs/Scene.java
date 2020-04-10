package ecs;

import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.util.Physics;
import ecs.util.Vector3;

public class Scene {
    public Scene() {
        Engine engine = new Engine(this);
        Entity entity = new Entity();
//        entity.AddComponent(Transform.class);
    }



}
