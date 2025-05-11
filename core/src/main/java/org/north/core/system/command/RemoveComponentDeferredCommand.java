package org.north.core.system.command;

import org.north.core.component.Component;
import org.north.core.architecture.entity.Entity;
import org.north.core.management.SystemManager;

public class RemoveComponentDeferredCommand implements DeferredCommand {
    public final Entity entity;
    public final Component component;

    public RemoveComponentDeferredCommand(Entity entity, Component component) {
        this.entity = entity;
        this.component = component;
    }

    @Override
    public void execute(SystemManager systemManager) {
        component.setActivity(false);
        systemManager.getSystemByComponentType(component.getClass()).removeComponent(component.getId());
    }

    @Override
    public CommandType getType() { return CommandType.REMOVE_COMPONENT; }
}
