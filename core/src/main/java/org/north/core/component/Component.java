package org.north.core.component;

import org.north.core.architecture.entity.Entity;
import org.north.core.management.data.Stateful;

public interface Component extends Stateful<ComponentState> {

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

    default Transform getTransform() {
        return getEntity().getTransform();
    }
}
 