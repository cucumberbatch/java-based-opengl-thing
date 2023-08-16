package ecs.graphics;

import ecs.components.MeshRenderer;
import ecs.components.Transform;
import ecs.systems.Input;
import ecs.systems.TransformSystem;
import ecs.utils.Logger;
import ecs.utils.TerminalUtils;
import matrices.Matrix4f;
import org.lwjgl.glfw.GLFW;
import vectors.Vector2f;
import vectors.Vector3f;
import vectors.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {
    public Window window;

    public Shader   lastRenderedShader;
    public Mesh     lastRenderedMesh;
    public Material lastRenderedMaterial;

    public Shader   basicShader;

    public static Matrix4f projection = Matrix4f.identity();
    public static Matrix4f view = Matrix4f.lookAt(Vector3f.backward(), Vector3f.forward(), Vector3f.up());

    public Graphics(Window window) {
        this.basicShader = Shader.BACKGROUND;
        this.window = window;
    }

    public void render(MeshRenderer renderer) {
        Transform transform = renderer.transform;
        Transform worldTransform = TransformSystem.getWorldTransform(transform);
        renderer.shader.setUniform("u_position", worldTransform.position);
        Renderer2D.draw(renderer.mesh.vertexArray, renderer.texture, renderer.shader);
    }

    public void drawMesh(Mesh mesh) {
        //todo
    }

    private Vector3f cameraPosition = Vector3f.zero();
    private float ratio = 0f;

    private Vector2f cursorPosition = null;


    public void drawMesh(Mesh mesh, Vector4f color, Transform transform) {
        if (cursorPosition == null) {
            cursorPosition = Input.getCursorPosition();
            Logger.info("window height: " + Window.height);
            Logger.info("window width: " + Window.width);
        }

        if (basicShader == null)
            basicShader = Shader.BACKGROUND;

        cursorPosition = Input.getCursorPosition();

        Matrix4f model = Matrix4f.translation(transform.position)
//                .mul(Matrix4f.rotation(1, transform.rotation))
                .mul(Matrix4f.scale(transform.scale));

        Logger.debug("model matrix:" + TerminalUtils.formatOutputMatrix(model) + "\n");
        Logger.debug("view matrix:" + TerminalUtils.formatOutputMatrix(view) + "\n");
        Logger.debug("projection matrix:" + TerminalUtils.formatOutputMatrix(projection) + "\n");


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

    public static void setProjection(Matrix4f projection) {
        Graphics.projection = projection;
    }

    public static void setView(Matrix4f view) {
        Graphics.view = view;
    }
}
