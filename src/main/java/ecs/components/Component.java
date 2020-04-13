package ecs.components;

import ecs.entities.Entity;
import ecs.systems.System;
import ecs.util.Instantiatable;
import ecs.util.Replicable;
import ecs.util.Turntable;

public interface Component
        extends Turntable, Instantiatable<Component>, Replicable<Component> {

    /* Get and set methods for the name */
    String name();
    void name(String name);

    /* Get and set methods for an entity */
    Entity entity();
    void entity(Entity entity);

    /* Get and set methods for the system */
    System system();
    void system(System system);

    /* Get and set methods for the transform component */
    Transform transform();
    void transform(Transform transform);


}
 