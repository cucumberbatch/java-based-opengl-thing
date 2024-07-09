package org.north.core.component;

import org.north.core.architecture.entity.Entity;
import org.north.core.managment.data.Stateful;

import java.util.UUID;

public interface Component extends Stateful<ComponentState> {
    UUID getId();
    void setId(UUID id);

    Entity getEntity();
    void attachToEntity(Entity entity);

    boolean isActive();
    void setActivity(boolean activity);

    default void switchActivity() {
        setActivity(!isActive());
    }

    default Transform getTransform() {
        return getEntity().getTransform();
    }
}
 