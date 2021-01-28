package ecs.systems;

import ecs.components.Renderer;
import ecs.gl.Window;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.graphics.Renderer2D;

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
        Renderer2D.draw(currentComponent().background, currentComponent().texture, Shader.BACKGROUND);
    }
}
