package org.north.core.component;

import org.north.core.entity.Entity;

public interface Component {

    default String getSimpleName() {
        return this.getClass().getSimpleName();
    }

    Entity getEntity();
    void setEntity(Entity entity);

    boolean isActive();
    void setActivity(boolean activity);

    default void switchActivity() {
        setActivity(!isActive());
    }

    ComponentState getState();

    void setState(ComponentState state);

    default boolean inState(ComponentState state) {
        return getState().equals(state);
    }

    default Transform getTransform() {
        return getEntity().getTransform();
    }
}
 