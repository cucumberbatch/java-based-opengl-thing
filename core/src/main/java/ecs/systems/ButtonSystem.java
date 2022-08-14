package ecs.systems;

import ecs.components.Button;
import ecs.shapes.Rectangle;

public class ButtonSystem extends AbstractECSSystem<Button> {

    @Override
    public int getWorkflowMask() {
        return INIT_MASK;
    }

    @Override
    public void init() throws Exception {
        MeshCollider meshComponent = getComponent(Type.MESH_COLLIDER);
        meshComponent.mesh = new Rectangle(component.topLeft, component.bottomRight);
    }
}
