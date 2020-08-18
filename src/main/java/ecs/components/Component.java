package ecs.components;

import ecs.entities.Entity;
import ecs.systems.System;
import ecs.util.Instantiatable;
import ecs.util.Replicable;
import ecs.util.Turntable;

import java.util.UUID;

public interface Component
        extends Turntable, Instantiatable<Component>, Replicable<Component> {

    /* Get and set methods for Id of component */
    UUID id();

    void id(UUID Id);

    void reset();

    /* Get and set methods for the name */
    String name();

    void name(String name);


    /* Get and set methods for an entity */
    Entity entity();

    void entity(Entity entity);


    /* Get and set methods for the system */
    System<? extends Component> system();

    void system(System<? extends Component> system);


    /* Get and set methods for the transform component */
    Transform transform();

    void transform(Transform transform);


}
 