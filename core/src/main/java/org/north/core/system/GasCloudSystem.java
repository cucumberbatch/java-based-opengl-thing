package org.north.core.system;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.MeshRenderer;
import org.north.core.component.GasCloud;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.shader.AtlasTextureAnimationShader;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.UpdateProcess;

@ComponentHandler(GasCloud.class)
public class GasCloudSystem extends AbstractSystem<GasCloud> implements UpdateProcess<GasCloud> {

    @Inject
    public GasCloudSystem(ApplicationContext context) {
        super(GasCloud.class, context);
    }

    @Override
    public void update(GasCloud gasCloud, float deltaTime) {
        Entity entity = gasCloud.getEntity();
       
        MeshRenderer meshRenderer = cm.get(entity, MeshRenderer.class);
        AtlasTextureAnimationShader shader = (AtlasTextureAnimationShader) meshRenderer.shader;

        if (shader.spriteIndex > 4) {
            //todo: we need to completely remove entity,
            // so for now we just remove components of it
            cm.remove(entity, MeshRenderer.class);
            cm.remove(entity, GasCloud.class);
            //cm.remove(entity, Transform.class);

            // wierd operation, but anyway...
            entity.remove(entity);
        }

        if (gasCloud.acc > 1) {
            shader.incrementSpriteIndex();
            gasCloud.acc = 0;
        }

        gasCloud.acc += deltaTime * 7f;
    }
}
