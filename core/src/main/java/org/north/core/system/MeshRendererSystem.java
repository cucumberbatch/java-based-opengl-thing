package org.north.core.system;

import org.joml.Matrix4f;
import org.north.core.architecture.ApplicationContext;
import org.north.core.architecture.ComponentHandler;
import org.north.core.architecture.Inject;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Material;
import org.north.core.graphics.Mesh;
import org.north.core.scene.component.MeshRenderer;
import org.north.core.scene.component.Transform;
import org.north.core.system.process.RenderProcess;

@ComponentHandler(MeshRenderer.class)
public class MeshRendererSystem
        extends AbstractSystem<MeshRenderer>
        implements RenderProcess<MeshRenderer> {

    private final Matrix4f bufferedMatrix = new Matrix4f();

    @Inject
    public MeshRendererSystem(ApplicationContext context) {
        super(MeshRenderer.class, context);
    }

    @Override
    public void render(MeshRenderer meshRenderer, Graphics graphics) {
        Mesh mesh = meshRenderer.mesh;
        Material material = meshRenderer.material;
        if (mesh == null || material == null) return;

        Transform transform = cm.get(meshRenderer.getEntity(), Transform.class);

        mesh.upload(graphics);
        material.apply(graphics, transform.getGlobalModelMatrix(bufferedMatrix));

        graphics.drawIndexed(mesh.vao(), mesh.indexCount());
    }
}
