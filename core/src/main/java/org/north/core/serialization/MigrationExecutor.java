package org.north.core.serialization;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.Component;

import java.io.ObjectInputStream;

public class MigrationExecutor {
    private final ObjectInputStream in;

    public MigrationExecutor(ObjectInputStream in) {
        this.in = in;
    }

    public Entity readEntity() {
        return null;
    }

    public Component readComponent() {
        return null;
    }
}
