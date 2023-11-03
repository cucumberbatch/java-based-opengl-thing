package ecs.systems;

import ecs.components.MeshRenderer;
import ecs.components.Transform;
import ecs.graphics.Graphics;
import ecs.graphics.Mesh;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.reflection.ComponentHandler;
import ecs.systems.processes.RenderProcess;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

@ComponentHandler(MeshRenderer.class)
public class MeshRendererSystem extends AbstractSystem<MeshRenderer> implements RenderProcess {

    @Override
    public void render(Graphics graphics) {
        Transform transform = component.transform;
        Vector3f position = transform.position;
        Vector3f scale = transform.scale;
        Mesh mesh = component.mesh;
        Shader shader = component.shader;
        Texture texture = component.texture;
        boolean hasTexture = Objects.nonNull(texture);
        Vector4f color = component.color;
        int renderType = component.renderType;

        Matrix4f model = new Matrix4f().identity()
                .translate(position.x, position.y, position.z)
                .scale(scale.x, scale.y, scale.z);

        shader.setUniform("u_color", color);
        shader.setUniform("u_projection", graphics.projection);
        shader.setUniform("u_view", graphics.view);
        shader.setUniform("u_model", model);

        shader.enable();
        if (hasTexture) texture.bind();
        mesh.vertexArray.render(renderType);
        if (hasTexture) texture.unbind();
        shader.disable();
    }
}
