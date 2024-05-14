package org.north.core.component;

import org.north.core.architecture.entity.Entity;

import java.util.UUID;

public interface Component {
    UUID getId();
    void setId(UUID id);

    Entity getEntity();
    void attachToEntity(Entity entity);

    ComponentState getState();
    void setState(ComponentState state);

    default boolean inState(ComponentState state) {
        return getState().equals(state);
    }

    boolean isActive();
    void setActivity(boolean activity);

    default void switchActivity() {
        setActivity(!isActive());
    }

    default Transform getTransform() {
        return getEntity().getTransform();
    }
}
 