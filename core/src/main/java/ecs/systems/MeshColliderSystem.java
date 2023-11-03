package ecs.systems;

import ecs.components.Transform;
import ecs.physics.collision.MeshTransformListener;
import ecs.reflection.ComponentHandler;
import ecs.systems.processes.InitProcess;
import ecs.systems.processes.UpdateProcess;
import org.joml.Vector2f;

@ComponentHandler(MeshCollider.class)
public class MeshColliderSystem extends AbstractSystem<MeshCollider>
        implements InitProcess, UpdateProcess {

    private static final int xOffsetLeft = 10;
    private static final int yOffsetUp = 10;

    @Override
    public void init() {
        this.component.entity.transform.setTransformListener(new MeshTransformListener());
    }

    @Override
    public void update(float deltaTime) {
        Transform transform = component.transform;
        Vector2f position = new Vector2f(transform.position.x, transform.position.y);
        component.body.moveTo(position);
//        component.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.y - yOffsetUp);
//        component.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.y + yOffsetUp);
    }

}
