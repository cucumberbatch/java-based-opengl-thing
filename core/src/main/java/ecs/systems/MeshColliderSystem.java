package ecs.systems;

import ecs.components.Transform;

public class MeshColliderSystem extends AbstractSystem<MeshCollider> {

    private static final int xOffsetLeft = 10;
    private static final int yOffsetUp = 10;

    @Override
    public int getWorkflowMask() {
        return /*UPDATE_MASK | */COLLISION_MASK;
    }

    @Override
    public void update(float deltaTime) {
        Transform transform = component.transform;
        component.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.y - yOffsetUp);
        component.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.y + yOffsetUp);
    }

}
