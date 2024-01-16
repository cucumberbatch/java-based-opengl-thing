package org.north.core.systems;

import org.north.core.architecture.ComponentManager;
import org.north.core.architecture.entity.ImprovedEntityManager;
import org.north.core.components.MeshRenderer;
import org.north.core.components.GasCloud;
import org.north.core.components.Transform;
import org.north.core.graphics.*;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.UpdateProcess;

@ComponentHandler(GasCloud.class)
public class GasCloudSystem extends AbstractSystem<GasCloud> implements UpdateProcess {

    private final ImprovedEntityManager em = new ImprovedEntityManager(ComponentManager.getInstance());

    private float acc = 0;

    @Override
    public void update(float deltaTime) {
        MeshRenderer meshRenderer = em.take(component.entity).get(MeshRenderer.class);
        AtlasTextureAnimationShader shader = (AtlasTextureAnimationShader) meshRenderer.shader;

        if (shader.spriteIndex > 4) {
            em.take(component.entity).get(Transform.class)
                    .moveTo(0f, -0.225f, 1f);
        }

        if (acc > 1) {
            shader.incrementSpriteIndex();
            acc = 0;
        }
        acc += deltaTime * 8f;

        em.take(component.entity).get(Transform.class)
                .moveRel(0f, -0.025f, 0f);
    }
}
