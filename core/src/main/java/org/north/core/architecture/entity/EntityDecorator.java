package org.north.core.architecture.entity;

import java.util.UUID;

public class EntityDecorator {
    public void generateIdForEntity(Entity entity) {
        entity.setId(UUID.randomUUID());
    }
}
