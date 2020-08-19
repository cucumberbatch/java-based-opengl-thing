package ecs.systems;

import ecs.components.Renderer;
import ecs.gl.Window;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;

public class RendererSystem extends AbstractSystem<Renderer> {

    @Override
    public void init() throws Exception {
        Renderer renderer = currentComponent();
        renderer.background = new VertexArray(renderer.vertices, renderer.indices, renderer.uv);
        renderer.texture = new Texture("/textures/bg.jpeg");
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(Window window) {
        currentComponent().texture.bind();
        Shader.BACKGROUND.enable();
        currentComponent().background.render();
        Shader.BACKGROUND.disable();
        currentComponent().texture.unbind();
    }
}
