package ecs.systems;

import ecs.components.Sandbox;
import ecs.components.Transform;
import ecs.graphics.Graphics;
import ecs.graphics.Mesh;
import ecs.graphics.Shader;
import ecs.graphics.Window;
import ecs.reflection.ComponentHandler;
import ecs.systems.processes.InitProcess;
import ecs.systems.processes.RenderProcess;
import ecs.systems.processes.UpdateProcess;
import ecs.utils.Logger;
import ecs.utils.Stopwatch;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

@ComponentHandler(Sandbox.class)
public class SandboxSystem extends AbstractSystem<Sandbox> implements InitProcess, UpdateProcess, RenderProcess {

    private float acc;
    private Random rnd;

    private Matrix4f projection;
    private Matrix4f view;
    private Matrix4f model;

    @Override
    public void init() {
        rnd = new Random();
        component.mesh = new Mesh();
        component.shader = Shader.GUI;

        projection = new Matrix4f().ortho(-Window.width / 2, Window.width / 2, -Window.width / 2, Window.width / 2, -1, 1);

        view = new Matrix4f().setLookAt(
                0, 0, -1,
                0, 0, 0,
                0, 1, 0
        );

    }

    @Override
    public void update(float deltaTime) {
        Stopwatch.start();
        Transform transform = component.transform;
        Vector3f position = transform.position;
        Vector3f scale = transform.scale;

        Vector2f cursorPosition = Input.getCursorPosition().sub(Window.width / 2f, Window.width / 2f);

        transform.moveTo(-cursorPosition.x, -cursorPosition.y, 0);
        transform.scale.set(50f, 50f, 50f);

        acc += deltaTime * 3;
        float sin = (float) Math.sin(acc);

        component.color.set(
                sin * 0.5f + 0.55f,
                sin * 0.2f + 0.57f,
                sin * 0.1f + 0.7f,
                1.0f
        );

        model = new Matrix4f()
                .translation(position.x, position.y, position.z)
                .scale(scale.x, scale.y, scale.z);

        Stopwatch.stop("Sandbox updated");
    }

    @Override
    public void render(Graphics graphics) {
        Mesh mesh = component.mesh;
        Shader shader = component.shader;
        Vector4f color = component.color;

        shader.setUniform("u_color", color);
        shader.setUniform("u_projection", projection);
        shader.setUniform("u_view", view);
        shader.setUniform("u_model", model);


        shader.enable();
        mesh.vertexArray.render(GL_TRIANGLES);
        shader.disable();
    }
}
