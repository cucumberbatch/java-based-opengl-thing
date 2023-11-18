package org.north.core.systems;

import org.north.core.components.Renderer;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Renderer2D;
import org.north.core.graphics.OldShader;
import org.north.core.graphics.VertexArray;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.RenderProcess;
import org.north.core.systems.processes.UpdateProcess;

@ComponentHandler(Renderer.class)
public class RendererSystem extends AbstractSystem<Renderer>
        implements InitProcess, UpdateProcess, RenderProcess {

    @Override
    public void init() throws RuntimeException {
        Renderer renderer = getComponent();
        renderer.background = new VertexArray(renderer.vertices, renderer.indices, renderer.uv);
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(Graphics graphics) {
//        Shader.TEST.setUniform("u_texture", component.texture.getId());

//        Shader.TEST.setUniform("uColor", 1f, 1f, 1f, 1f);
//        Shader.TEST.setUniform("u_position", component.transform.position);

//        component.transform.position.add(0.01f, 0.01f, 0.01f);


//        Shader.BACKGROUND.setUniform("u_model",
//                Matrix4f.mul(
//                        Matrix4f.mul(
//                                Matrix4f.rotateAroundOY(component.transform.rotation.y),
//                                Matrix4f.rotateAroundOX(component.transform.rotation.x)),
//                        Matrix4f.translation(component.transform.position)));

//        Shader.BACKGROUND.setUniform("u_view", Matrix4f.identity());

//        Shader.BACKGROUND.setUniform("u_projection", Matrix4f.orthographic(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f));
//        Shader.BACKGROUND.setUniform("u_projection", Matrix4f.perspective(100f, 0.1f, 100.0f, 16.0f / 9.0f));

        Renderer2D.draw(component.background, component.texture, OldShader.TEXTURE_SHADER);
    }
}
