package ecs.graphics;

import ecs.Engine;
import ecs.components.MeshRenderer;
import ecs.components.Transform;
import ecs.config.EngineConfig;
import ecs.systems.Input;
import ecs.systems.TransformSystem;
import ecs.utils.Logger;
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

//    public Matrix4f projection = Matrix4f.perspective(90, -1f, 1f, 1);
    public Matrix4f projection = Matrix4f.perspective2(90, -1f, 1, 1);
//    public Matrix4f projection = Matrix4f.orthographic(-1, 1, -1, 1, -1, 1);
//    public Matrix4f projection = Matrix4f.orthographic2(-1, 1, -1, 1, -1, 1);

    public Vector3f cameraDirection = Vector3f.forward();

    public Matrix4f view = Matrix4f.lookAt(Vector3f.backward(), Vector3f.forward(), Vector3f.up());

    public Vector3f experimentVector = Vector3f.zero();

    public Graphics() {
        this.basicShader = Shader.BACKGROUND;
    }

    public void render(MeshRenderer renderer) {
        Transform transform = renderer.transform;
        Transform worldTransform = TransformSystem.getWorldTransform(transform);
        renderer.shader.setUniform("u_position", worldTransform.position);
        Renderer2D.draw(renderer.mesh.vertexArray, renderer.texture, renderer.shader);
    }

    // 1 = sin2(x) + cos2(x)
//    public float circleEquation(float x, float y) {
//
//    }

    public void drawMesh(Mesh mesh) {
        //todo
    }

    private Vector3f cameraPosition = Vector3f.zero();
    private float ratio = 0f;

    private Vector2f cursorPosition = null;

    public void drawMesh(Mesh mesh, Vector4f color, Transform transform) {

//        projection = Matrix4f.lerp(
//                Matrix4f.perspective2(90, -1f, 1, 1),
//                Matrix4f.orthographic(1, -1, 1, -1, 1, -1),
//                (float) Math.sin(ratio)
//        );


        if (cursorPosition == null) {
            cursorPosition = Input.getCursorPosition();
            Logger.info("window height: " + Window.height);
            Logger.info("window width: " + Window.width);
        }

//        Vector2f cursorPositionDiff = cursorPosition.sub(Input.getCursorPosition());

//        experimentVector.set(
//                 Input.getCursorPosition().x / (Window.width / 2) - 1f,
//                -Input.getCursorPosition().y / (Window.height / 2) + 1f,
//                0f
//        );




        ratio += 0.05f;

//        cameraRotation.set(Vector3f.add(cameraPosition, new Vector3f((float) (Math.cos(ratio)), (float) (Math.sin(ratio)), 0f)));
        Vector3f cameraFocus = Vector3f.add(cameraPosition, new Vector3f(0f, 0f, 1f));
        view = Matrix4f.lookAt(cameraPosition, cameraFocus, Vector3f.up());

//        cameraRotation.set(cameraPosition.x + (float) Math.sin(ratio) - 0.5f, cameraPosition.y, 0f);

        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            cameraPosition.add(0f, 0f, 0.02f);
        }

        if (Input.isHeldDown(GLFW.GLFW_KEY_S)) {
            cameraPosition.add(0f, 0f, -0.02f);
        }

        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            cameraPosition.add(-0.02f, 0f, 0f);
        }

        if (Input.isHeldDown(GLFW.GLFW_KEY_D)) {
            cameraPosition.add(0.02f, 0f, 0f);
        }
//
//        Vector2f cursorPosition = Input.getCursorPosition();
//        view = Matrix4f.lookAt(cameraPosition, new Vector3f(cursorPosition.x, cursorPosition.y, 0f).div(Window.width), Vector3f.up());


//        Matrix4f translation = Matrix4f.translation(transform.position);
//        Matrix4f translatedMatrix = Matrix4f.multiply(Matrix4f.multiply(projection, view), translation);

        if (basicShader == null)
            basicShader = Shader.BACKGROUND;


        Vector2f rotationVectorResult = Vector2f.sub(cursorPosition, Input.getCursorPosition());
        cursorPosition = Input.getCursorPosition();

//        Matrix4f model = Matrix4f.translation(transform.position);
        Matrix4f model = Matrix4f.translation(experimentVector, new Vector3f(Input.getCursorPosition().div(2), 0f), Vector3f.one().mul(0.7f));
//        Matrix4f model = Matrix4f.translation(experimentVector, new Vector3f(0.0f, 0.0f, 0.0f), Vector3f.one());

//        model = Matrix4f.multiply(model, Matrix4f.rotateAroundOY(Input.getCursorPosition().x));
//        model = Matrix4f.multiply(model, Matrix4f.rotateAroundOX(Input.getCursorPosition().y));

//        Matrix4f projection = Matrix4f.perspective(10, 0, 1, 1);

        basicShader.setUniform("u_color", color);

        basicShader.setUniform("u_projection", projection);
        basicShader.setUniform("u_view",       view);
        basicShader.setUniform("u_model",      model);

        this.enableShaderAndDrawMesh(basicShader, mesh);
    }

    private void enableShaderAndDrawMesh(Shader shader, Mesh mesh) {
        shader.enable();
//        mesh.vertexArray.render(GL_TRIANGLE_STRIP);
//        mesh.vertexArray.render(GL_LINE_STRIP);
        mesh.vertexArray.render(GL_TRIANGLES);
//        mesh.vertexArray.render(GL_LINES);
//        mesh.vertexArray.render(GL_QUADS);
//        mesh.vertexArray.render(GL_QUAD_STRIP);
        shader.disable();
    }
}
