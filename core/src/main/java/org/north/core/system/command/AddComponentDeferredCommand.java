package org.north.core.system.command;

import org.north.core.component.Component;
import org.north.core.architecture.entity.Entity;
import org.north.core.managment.SystemManager;

public class AddComponentDeferredCommand implements DeferredCommand {
    public final Entity entity;
    public final Component component;

    public AddComponentDeferredCommand(Entity entity, Component component) {
        this.entity = entity;
        this.component = component;
    }

    @Override
    public void execute(SystemManager systemManager) {
        systemManager.addComponent(component);
    }

    @Override
    public CommandType getType() { return CommandType.ADD_COMPONENT; }
}
