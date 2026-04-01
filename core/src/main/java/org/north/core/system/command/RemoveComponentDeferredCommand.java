package org.north.core.system.command;

import org.north.core.component.Component;
import org.north.core.entity.Entity;
import org.north.core.management.SystemManager;

public class RemoveComponentDeferredCommand implements DeferredCommand {
    public final Entity entity;
    public final Component component;
    public final Class<? extends Component> componentType;

    public RemoveComponentDeferredCommand(Entity entity, Component component) {
        this.entity = entity;
        this.component = component;
        this.componentType = component.getClass();
    }

    @Override
    public void execute(SystemManager systemManager) {
        component.setActivity(false);
        systemManager.getSystemByComponentType(componentType).removeComponent(component);
    }

    @Override
    public CommandType getType() { return CommandType.REMOVE_COMPONENT; }
}
