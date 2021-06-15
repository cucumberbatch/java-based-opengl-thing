package ecs.systems;

import ecs.components.Renderer;
import ecs.gl.Window;
import ecs.graphics.Renderer2D;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.math.Matrix4f;
import ecs.math.Vector3f;

public class RendererSystem extends AbstractECSSystem<Renderer> {

    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK | RENDER_MASK;
    }

    @Override
    public void init() throws Exception {
        Renderer renderer = getCurrentComponent();
        renderer.background = new VertexArray(renderer.vertices, renderer.indices, renderer.uv);
        renderer.texture = new Texture("textures/screen-frame-1024.png");

        currentComponent.transform.position.set(1f, 0f, 0f);
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
                                Matrix4f.rotation(Vector3f.up(),    currentComponent.transform.rotation.y),
                                Matrix4f.rotation(Vector3f.right(), currentComponent.transform.rotation.x)),
                        Matrix4f.translation(currentComponent.transform.position)));

//        Shader.BACKGROUND.setUniformMat4f("u_view", Matrix4f.identity());

        Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.orthographic(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f));
//        Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.perspective(100f, 0.1f, 100.0f, 16.0f / 9.0f));

        Renderer2D.draw(getCurrentComponent().background, getCurrentComponent().texture, Shader.BACKGROUND);
    }
}
