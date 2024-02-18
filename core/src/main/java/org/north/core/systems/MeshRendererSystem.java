package org.north.core.systems;

import org.north.core.components.MeshRenderer;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.*;
import org.north.core.graphics.shader.Shader;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.systems.processes.RenderProcess;

@ComponentHandler(MeshRenderer.class)
public class MeshRendererSystem extends AbstractSystem<MeshRenderer> implements RenderProcess {

    @Inject
    public MeshRendererSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void render(Graphics graphics) {
        Shader shader = component.shader;
        VertexArray vertexArray = component.mesh.vertexArray;
        int renderType = component.renderType;

        shader.enable();
        shader.prepareShader(graphics, component);
        vertexArray.render(renderType);
        shader.disable();

    }
}
