package org.north.core.system;

import org.north.core.component.MeshRenderer;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.*;
import org.north.core.graphics.shader.Shader;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.RenderProcess;

@ComponentHandler(MeshRenderer.class)
public class MeshRendererSystem extends AbstractSystem<MeshRenderer> implements RenderProcess<MeshRenderer> {

    @Inject
    public MeshRendererSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void render(MeshRenderer meshRenderer, Graphics graphics) {
        Shader shader = meshRenderer.shader;
        VertexArray vertexArray = meshRenderer.mesh.vertexArray;
        int renderType = meshRenderer.renderType;

        shader.enable();
        shader.prepareShader(graphics, meshRenderer);
        vertexArray.render(renderType);
        shader.disable();

    }
}
