package org.north.core.systems;

import org.north.core.components.MeshRenderer;
import org.north.core.components.GasCloud;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.shader.AtlasTextureAnimationShader;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.systems.processes.UpdateProcess;

@ComponentHandler(GasCloud.class)
public class GasCloudSystem extends AbstractSystem<GasCloud> implements UpdateProcess {

    private float acc = 0;

    @Inject
    public GasCloudSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void update(float deltaTime) {
        MeshRenderer meshRenderer = cm.take(component.entity).get(MeshRenderer.class);
        AtlasTextureAnimationShader shader = (AtlasTextureAnimationShader) meshRenderer.shader;

        if (shader.spriteIndex > 4) {
//            cm.take(component.entity).get(Transform.class)
//                    .moveTo(0f, -0.225f, 1f);

            cm.take(component.entity).remove(MeshRenderer.class);
        }

        if (acc > 1) {
            shader.incrementSpriteIndex();
            acc = 0;
        }
        acc += deltaTime * 6f;

//        cm.take(component.entity).get(Transform.class)
//                .moveRel(0f, -deltaTime, 0f);
    }
}
