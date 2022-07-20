package ecs.systems;

import ecs.components.Renderer;
import ecs.gl.Window;
import ecs.graphics.Renderer2D;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import matrices.Matrix4f;

public class RendererSystem extends AbstractECSSystem<Renderer> {

    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK | RENDER_MASK;
    }

    @Override
    public void init() throws Exception {
        Renderer renderer = getComponent();
        renderer.background = new VertexArray(renderer.vertices, renderer.indices, renderer.uv);
        renderer.texture = new Texture("textures/screen-frame-1024.png");
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(Window window) {
        Shader.BACKGROUND.setUniform1i("u_tex", Shader.BACKGROUND.getId());

        Shader.BACKGROUND.setUniformMat4f("u_model",
                Matrix4f.multiply(
                        Matrix4f.multiply(
                                Matrix4f.rotateAroundOY(component.transform.rotation.y),
                                Matrix4f.rotateAroundOX(component.transform.rotation.x)),
                        Matrix4f.translation(component.transform.position)));

//        Shader.BACKGROUND.setUniformMat4f("u_view", Matrix4f.identity());

        Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.orthographic(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f));
//        Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.perspective(100f, 0.1f, 100.0f, 16.0f / 9.0f));

        Renderer2D.draw(getComponent().background, getComponent().texture, Shader.BACKGROUND);
    }
}
