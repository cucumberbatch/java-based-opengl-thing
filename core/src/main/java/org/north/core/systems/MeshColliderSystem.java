package org.north.core.systems;

import org.north.core.components.MeshCollider;
import org.north.core.components.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.physics.collision.MeshTransformListener;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.UpdateProcess;
import org.joml.Vector2f;

@ComponentHandler(MeshCollider.class)
public class MeshColliderSystem extends AbstractSystem<MeshCollider>
        implements InitProcess, UpdateProcess {

    private static final int xOffsetLeft = 10;
    private static final int yOffsetUp = 10;

    @Inject
    public MeshColliderSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init() {
        this.component.entity.transform.setTransformListener(new MeshTransformListener());
    }

    @Override
    public void update(float deltaTime) {
        Transform transform = component.getTransform();
        Vector2f position = new Vector2f(transform.position.x, transform.position.y);
        component.body.moveTo(position);
//        component.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.y - yOffsetUp);
//        component.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.y + yOffsetUp);
    }

}
