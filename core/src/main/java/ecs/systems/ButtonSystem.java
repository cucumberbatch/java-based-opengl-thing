package ecs.systems;

import ecs.components.Button;
import ecs.shapes.Rectangle;

public class ButtonSystem extends AbstractSystem<Button> {

    @Override
    public int getWorkflowMask() {
        return INIT_MASK;
    }

    @Override
    public void init() throws RuntimeException {
        MeshCollider meshComponent = componentManager.getComponent(component.entity, MeshCollider.class);
        meshComponent.mesh = new Rectangle(component.topLeft, component.bottomRight);
    }
}
