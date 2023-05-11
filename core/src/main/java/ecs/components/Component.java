package ecs.components;

import ecs.entities.Entity;
import ecs.utils.Instantiatable;
import ecs.utils.Replicable;
import ecs.utils.Turntable;

public interface Component
        extends Turntable, Instantiatable<Component>, Replicable<Component> {

    /* Get and set methods for Id of component */
    long getId();

    void setId(int id);

    void reset();

    /* Get and set methods for the name */
    String getName();

    void setName(String name);


    /* Get and set methods for an entity */
    Entity getEntity();

    void setEntity(Entity entity);

    /* Get and set methods for the transform component */
    Transform getTransform();

    void setTransform(Transform transform);

    byte getState();

    void setState(byte state);

}
 