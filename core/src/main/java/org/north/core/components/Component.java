package org.north.core.components;

import org.north.core.architecture.entity.Entity;
import org.north.core.components.serialization.Serializable;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Component extends Serializable<ObjectOutputStream, ObjectInputStream> {

    /* Get and set methods for Id of component */
    long getId();

    void setId(int id);

    void reset();

    /* Get and set methods for an entity */
    Entity getEntity();

    void setEntity(Entity entity);

    Transform getTransform();

    ComponentState getState();

    void setState(ComponentState state);

    boolean inState(ComponentState state);

    boolean isActive();

    void setActivity(boolean activity);

    void switchActivity();
}
 