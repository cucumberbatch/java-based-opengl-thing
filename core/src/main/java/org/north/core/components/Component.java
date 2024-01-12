package org.north.core.components;

import org.north.core.entities.Entity;

public interface Component {

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

    ComponentState getState();

    void setState(ComponentState state);

    boolean inState(ComponentState state);

    boolean isActive();

    void setActivity(boolean activity);

    void switchActivity();
}
 