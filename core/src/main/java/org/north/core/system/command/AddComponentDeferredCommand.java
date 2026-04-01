package org.north.core.system.command;

import org.north.core.component.Component;
import org.north.core.entity.Entity;
import org.north.core.management.SystemManager;

public class AddComponentDeferredCommand implements DeferredCommand {
    private final Entity entity;
    private final Component component;

    public AddComponentDeferredCommand(Entity entity, Component component) {
        this.entity = entity;
        this.component = component;
    }

    @Override
    public void execute(SystemManager systemManager) {
        systemManager.addComponent(component);
    }

    @Override
    public CommandType getType() {
        return CommandType.ADD_COMPONENT;
    }

    @Override
    public String toString() {
        return "AddComponentDeferredCommand{" +
                "entity=" + entity +
                ", component=" + component +
                '}';
    }
}
