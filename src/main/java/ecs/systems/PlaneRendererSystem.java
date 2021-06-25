package ecs.systems;

import ecs.components.PlaneRenderer;
import ecs.graphics.gl.Window;
import ecs.graphics.Renderer2D;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.math.Matrix4f;
import ecs.math.Vector3f;

public class PlaneRendererSystem extends AbstractECSSystem<PlaneRenderer> {

    public PlaneRendererSystem() {
        setWorkflowMask(INIT_MASK | UPDATE_MASK | RENDER_MASK);
    }

    @Override
    public void onInit() {
        PlaneRenderer renderer = getComponent();
        renderer.background = new VertexArray(renderer.vertices, renderer.indices, renderer.uv);
        renderer.texture = new Texture("textures/screen-frame-1024.png");

//        currentComponent.transform.position.set(1f, 0f, 0f);
    }

    @Override
    public void onUpdate(float deltaTime) {
    }

    @Override
    public void onRender(Window window) {
        Shader.BACKGROUND.setUniform1i("u_tex", Shader.BACKGROUND.getId());

        Shader.BACKGROUND.setUniformMat4f("u_model",
                Matrix4f.multiply(
                        Matrix4f.multiply(
                                Matrix4f.rotation(Vector3f.up(),    component.transform.rotation.y),
                                Matrix4f.rotation(Vector3f.right(), component.transform.rotation.x)),
                        Matrix4f.translation(component.transform.position)));

//        Shader.BACKGROUND.setUniformMat4f("u_view", Matrix4f.identity());

        Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.orthographic(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f));
//        Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.perspective(50f, 0.1f, 100.0f, 0f));

        Renderer2D.draw(getComponent().background, getComponent().texture, Shader.BACKGROUND);

    }

}