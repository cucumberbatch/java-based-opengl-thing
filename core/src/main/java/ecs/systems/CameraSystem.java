package ecs.systems;

import ecs.graphics.Graphics;
import ecs.graphics.PredefinedMeshes;
import ecs.components.Camera;
import ecs.graphics.Window;
import ecs.utils.Logger;
import matrices.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import vectors.Vector2f;
import vectors.Vector3f;
import vectors.Vector4f;


public class CameraSystem extends AbstractSystem<Camera> {

    float near = 0.1f;
    float far = 100f;
    float angle = 180f;
    float ratio = 16f / 9f;

    Vector3f planePosition = new Vector3f(1f, 0f, -1f); //currentComponent.entity.getEngine().getEntityByName("side_plane").transform.position;


    Vector3f cameraPosition = new Vector3f();
    Vector3f point = Vector3f.zero();

    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK | RENDER_MASK;
    }

    @Override
    public void init() throws RuntimeException {
        Logger.info(String.format("%s: started initialization..", this.getClass().getName()));
        component.eye.set(2f, 0f, -3f);
        component.up.set(Vector3f.up());
        component.at.set(new Vector3f(1.0f, 0.0f, -1.0f).add(component.eye));
    }

    @Override
    public void update(float deltaTime) {
        Vector2f cursorPosition = Input.getCursorPosition();

        float verticalAngle   = +cursorPosition.y / (Window.width / 256f) - 180;
        float horizontalAngle = -cursorPosition.x / (Window.width / 256f) - 180;

        float restrictedVerticalAngle = restrictAngle(verticalAngle, -89.9f, 89.9f);

        Vector3f point = Vector3f.forward().rotation(restrictedVerticalAngle, horizontalAngle, 0);

        float speedFactor;

        if (Input.isHeldDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            speedFactor = 3f;
        } else {
            speedFactor = 1f;
        }

        if (Input.isHeldDown(GLFW.GLFW_KEY_D)) {
            component.transform.position.add(deltaTime * speedFactor, 0, 0);
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            component.transform.position.add(-deltaTime * speedFactor, 0, 0);
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            component.transform.position.add(0, deltaTime * speedFactor, 0);
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_S)) {
            component.transform.position.add(0, -deltaTime * speedFactor, 0);
        }

        component.viewMatrix = Matrix4f.lookAt(cameraPosition, point, Vector3f.up());

        Graphics.setView(component.viewMatrix);
    }

    @Override
    public void render(Graphics graphics) {
        if (Input.isPressed(GLFW.GLFW_KEY_ESCAPE)) {
            GLFW.glfwSetInputMode(graphics.window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }

        if (Input.isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            GLFW.glfwSetInputMode(graphics.window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            Logger.info("Left mouse button is pressed!");
        }

        graphics.drawMesh(PredefinedMeshes.CUBE, Vector4f.one(), component.transform);
    }

    private float restrictAngle(float angle, float lowest, float highest) {
        return Math.min(Math.max(angle, lowest), highest);
    }
}
