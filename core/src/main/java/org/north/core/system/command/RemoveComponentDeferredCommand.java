package org.north.core.system.command;

import org.north.core.component.Component;
import org.north.core.entity.Entity;
import org.north.core.management.SystemManager;

public class RemoveComponentDeferredCommand implements DeferredCommand {
    public final Entity entity;
    public final Class<? extends Component> componentType;
    public final Component component;

    public RemoveComponentDeferredCommand(Entity entity, Component component) {
        this.entity = entity;
        this.componentType = component.getClass();
        this.component = component;
    }

    @Override
    public void execute(SystemManager systemManager) {
        component.setActivity(false);
        systemManager.getSystemByComponentType(componentType).removeComponent(component);
    }

    @Override
    public String toString() {
        return "RemoveComponentDeferredCommand{" +
                "entity=" + entity +
                ", componentType=" + componentType +
                ", component=" + component +
                '}';
    }
}
