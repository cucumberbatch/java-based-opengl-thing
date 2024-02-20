package org.north.core.component;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.serialization.Serializable;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public interface Component extends Serializable<ObjectOutputStream, ObjectInputStream> {
    UUID getId();
    void setId(UUID id);
    void reset();
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
 