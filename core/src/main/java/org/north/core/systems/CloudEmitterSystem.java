package org.north.core.systems;

import org.joml.Vector3f;
import org.north.core.architecture.ComponentManager;
import org.north.core.architecture.TreeEntityManager;
import org.north.core.architecture.entity.ImprovedEntityManager;
import org.north.core.components.CloudEmitter;
import org.north.core.components.GasCloud;
import org.north.core.components.MeshRenderer;
import org.north.core.components.Transform;
import org.north.core.architecture.entity.Entity;
import org.north.core.graphics.shader.AtlasTextureAnimationShader;
import org.north.core.graphics.Texture;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.UpdateProcess;

@ComponentHandler(CloudEmitter.class)
public class CloudEmitterSystem extends AbstractSystem<CloudEmitter> implements UpdateProcess {

    private final ImprovedEntityManager em = new ImprovedEntityManager(TreeEntityManager.getInstance(), ComponentManager.getInstance());
    private final Vector3f emittingPosition = new Vector3f(0f, -0.225f, 1f);
    private final Vector3f emittingScale = new Vector3f(0.13f, 0.13f, 0.13f);

    private float acc = 0;
    private long gasCloudEntityNumber = 0;
    private int gasCloudCount = 0;


    @Override
    public void update(final float deltaTime) {
        if (gasCloudCount > 6) return;

        if (acc > 1) {
            Entity gasCloudEntity = em.create("gas_cloud_" + gasCloudEntityNumber++);

            Transform transform = em.take(gasCloudEntity).add(Transform.class);
            MeshRenderer renderer = em.take(gasCloudEntity).add(MeshRenderer.class);
            GasCloud gasCloud = em.take(gasCloudEntity).add(GasCloud.class);

            transform.position.set(emittingPosition);
            transform.scale.set(emittingScale);

            renderer.shader = new AtlasTextureAnimationShader(6, 12, 12);
            renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

            gasCloudCount++;
            acc = 0;
        }

        acc += deltaTime * 3;
    }
}
