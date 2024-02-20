package org.north.core.system;

import org.north.core.component.MeshCollider;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.physics.collision.MeshTransformListener;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;
import org.joml.Vector2f;

@ComponentHandler(MeshCollider.class)
public class MeshColliderSystem extends AbstractSystem<MeshCollider>
        implements InitProcess<MeshCollider>, UpdateProcess<MeshCollider> {

    private static final int xOffsetLeft = 10;
    private static final int yOffsetUp = 10;

    @Inject
    public MeshColliderSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(MeshCollider meshCollider) {
        meshCollider.entity.transform.setTransformListener(new MeshTransformListener());
    }

    @Override
    public void update(MeshCollider meshCollider, float deltaTime) {
        Transform transform = meshCollider.getTransform();
        Vector2f position = new Vector2f(transform.position.x, transform.position.y);
        meshCollider.body.moveTo(position);
//        component.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.y - yOffsetUp);
//        component.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.y + yOffsetUp);
    }

}
