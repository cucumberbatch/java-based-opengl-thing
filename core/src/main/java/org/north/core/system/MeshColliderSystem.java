package org.north.core.system;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.north.core.scene.component.MeshCollider;
import org.north.core.scene.component.Transform;
import org.north.core.architecture.ApplicationContext;
import org.north.core.architecture.ComponentHandler;
import org.north.core.architecture.Inject;
import org.north.core.physics.shape.Rectangle;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

@ComponentHandler(MeshCollider.class)
public class MeshColliderSystem extends AbstractSystem<MeshCollider>
        implements InitProcess<MeshCollider>, UpdateProcess<MeshCollider> {

    private final Vector3f position = new Vector3f();
    private final Vector3f scale    = new Vector3f();

    @Inject
    public MeshColliderSystem(ApplicationContext context) {
        super(MeshCollider.class, context);
    }

    @Override
    public void init(MeshCollider meshCollider) {
        Transform transform = cm.get(meshCollider.getEntity(), Transform.class);
        if (transform == null) return;

        if (meshCollider.body == null) {
            meshCollider.body = new Rectangle(new Vector2f(), new Vector2f());
        }

        if (meshCollider.body.isEmpty()) {
            transform.getGlobalScale(scale);
            float width  = Math.abs(scale.x);
            float height = Math.abs(scale.y);

            meshCollider.body.topLeft.set(-width / 2, -height / 2);
            meshCollider.body.bottomRight.set(width / 2, height / 2);
        }
    }

    @Override
    public void update(MeshCollider meshCollider, float deltaTime) {
        Transform transform = cm.get(meshCollider.getEntity(), Transform.class);
        if (transform == null || meshCollider.body == null) return;

        transform.getGlobalPosition(position);
        meshCollider.body.moveTo(position.x, position.y);
    }

}
