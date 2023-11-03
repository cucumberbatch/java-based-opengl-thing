package ecs.graphics;

import ecs.components.MeshRenderer;
import ecs.components.Transform;
import ecs.systems.Input;
import ecs.systems.TransformSystem;
import ecs.utils.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {
    private static Graphics instance;

    public Window window;

    public Shader   lastRenderedShader;
    public Mesh     lastRenderedMesh;
    public Material lastRenderedMaterial;

    public Shader   basicShader;

    public Matrix4f projection = new Matrix4f().identity();
    public Matrix4f view = new Matrix4f().lookAt(new Vector3f(0, 0, -1), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));

    // implement dependency injection
    public static Graphics getInstance(Window window) {
        if (instance == null) {
            instance = new Graphics(window);
        }
        return instance;
    }

    private Graphics(Window window) {
        this.basicShader = Shader.BACKGROUND;
        this.window = window;
    }

    public void render(MeshRenderer renderer) {
        Transform transform = renderer.transform;
        Transform worldTransform = TransformSystem.getWorldTransform(transform);
//        renderer.shader.setUniform("u_position", worldTransform.position);
        Renderer2D.draw(renderer.mesh.vertexArray, renderer.texture, renderer.shader);
    }

    public void draw(MeshRenderer renderer) {
        renderer.shader.setUniform("u_color", new Vector3f(1, 1, 1));
        Renderer2D.draw(renderer.mesh.vertexArray, renderer.shader);
    }

    public void drawMesh(Mesh mesh) {
        //todo
    }

    private Vector3f cameraPosition = new Vector3f().zero();
    private float ratio = 0f;

    private Vector2f cursorPosition = null;

    public void draw(Mesh mesh, Shader shader, Vector4f color, Transform transform) {

    }

    public void drawMesh(Mesh mesh, Vector4f color, Transform transform) {
        if (cursorPosition == null) {
            cursorPosition = Input.getCursorPosition();
            Logger.info("window height: " + Window.height);
            Logger.info("window width: " + Window.width);
        }

        if (basicShader == null)
            basicShader = Shader.BACKGROUND;

        cursorPosition = Input.getCursorPosition();

        Transform worldTransform = TransformSystem.getWorldTransform(transform);

        Matrix4f model = new Matrix4f()
                .translate(worldTransform.position)
                .rotate(1, transform.rotation)
                .scale(worldTransform.scale);

//        model = Matrix4f.mul(model, Matrix4f.rotateAroundOY(Input.getCursorPosition().x));
//        model = Matrix4f.mul(model, Matrix4f.rotateAroundOX(Input.getCursorPosition().y));

        basicShader.setUniform("u_color", color);

        basicShader.setUniform("u_projection", projection);
        basicShader.setUniform("u_view",       view);
        basicShader.setUniform("u_model",      model);

//        basicShader.setUniform("u_projection", Matrix4f.mul(projection, view).mul(model));

        this.enableShaderAndDrawMesh(basicShader, mesh);
    }

    private void enableShaderAndDrawMesh(Shader shader, Mesh mesh) {

        shader.enable();
//        mesh.vertexArray.render(GL_TRIANGLE_STRIP);
//        mesh.vertexArray.render(GL_LINE_STRIP);
//        mesh.vertexArray.render(GL_TRIANGLES);
//        mesh.vertexArray.render(GL_LINES);
        mesh.vertexArray.render(GL_LINE_LOOP);
//        mesh.vertexArray.render(GL_QUADS);
//        mesh.vertexArray.render(GL_QUAD_STRIP);
        shader.disable();
    }
}
