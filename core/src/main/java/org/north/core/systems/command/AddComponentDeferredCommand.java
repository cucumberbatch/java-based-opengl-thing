package org.north.core.systems.command;

import org.north.core.components.Component;
import org.north.core.entities.Entity;

public class AddComponentDeferredCommand implements DeferredCommand {
    public final Entity entity;
    public final Component component;

    public AddComponentDeferredCommand(Entity entity, Component component) {
        this.entity = entity;
        this.component = component;
    }

    @Override
    public CommandType getType() { return CommandType.ADD_COMPONENT; }
}
