package ecs.systems;

import ecs.components.Camera;
import ecs.components.CameraControls;
import ecs.config.EngineConfig;
import ecs.graphics.Graphics;
import ecs.graphics.Window;
import ecs.reflection.ComponentHandler;
import ecs.systems.processes.InitProcess;
import ecs.systems.processes.RenderProcess;
import ecs.systems.processes.UpdateProcess;
import ecs.utils.Logger;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL30;
import org.joml.Vector2f;
import org.joml.Vector3f;

@ComponentHandler(CameraControls.class)
public class CameraControlsSystem extends AbstractSystem<CameraControls>
        implements InitProcess, UpdateProcess {

    private static final int PERSPECTIVE_VIEW_STATE = 0;
    private static final int PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE = 1;
    private static final int ORTHOGRAPHIC_VIEW_STATE = 2;
    private static final int ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE = 3;

    private static int projectionState = ORTHOGRAPHIC_VIEW_STATE;
    private float projectionProgress = 0;

    private static Camera camera;

    private Graphics graphics;

    @Override
    public void init() {
        Window window = EngineConfig.instance.window;
        graphics = Graphics.getInstance(window);
        camera = component.entity.getComponent(Camera.class);
    }

    @Override
    public void update(float deltaTime) {
        updateCameraMovement(deltaTime);
        updateCameraProjection();
        updateScreenCapture(graphics);
    }

    private void updateScreenCapture(Graphics graphics) {
        if (Input.isPressed(GLFW.GLFW_KEY_ESCAPE)) {
            GLFW.glfwSetInputMode(graphics.window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }

        if (Input.isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            GLFW.glfwSetInputMode(graphics.window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        }
    }

    private void updateCameraProjection() {
        switch (projectionState) {
            case PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE: {
                projectionProgress += 0.05;
                camera.projectionMatrix = new Matrix4f(CameraSystem.PERSPECTIVE_MATRIX).lerp(CameraSystem.ORTHOGRAPHIC_MATRIX, (float) (Math.sin(projectionProgress) / Math.sin(1)));
                if (projectionProgress > 1) {
                    projectionProgress = 0;
                    projectionState = ORTHOGRAPHIC_VIEW_STATE;
                }
                graphics.projection = camera.projectionMatrix;
                break;
            }
            case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE: {
                projectionProgress += 0.05;
                camera.projectionMatrix = new Matrix4f(CameraSystem.ORTHOGRAPHIC_MATRIX).lerp(CameraSystem.PERSPECTIVE_MATRIX, (float) (Math.sin(projectionProgress) / Math.sin(1)));
                if (projectionProgress > 1) {
                    projectionProgress = 0;
                    projectionState = PERSPECTIVE_VIEW_STATE;
                }
                graphics.projection = camera.projectionMatrix;
                break;
            }
            case PERSPECTIVE_VIEW_STATE: {
                if (Input.isPressed(GLFW.GLFW_KEY_1)) {
                    projectionState = PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE;
                }
                break;
            }
            case ORTHOGRAPHIC_VIEW_STATE: {
                if (Input.isPressed(GLFW.GLFW_KEY_1)) {
                    projectionState = ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE;
                }
                break;
            }
        }
    }

    private void updateCameraMovement(float deltaTime) {
        Vector2f cursorPosition = Input.getCursorPosition();

        float verticalAngle   = cursorPosition.y / (Window.width / 256f) - 180;
        float horizontalAngle = -cursorPosition.x / (Window.width / 256f) - 180;

        float restrictedVerticalAngle = restrictAngle(verticalAngle, -89.9f, 89.9f);

        Vector3f point = new Vector3f(0, 0, 1)
                .rotateX((float) Math.toRadians(restrictedVerticalAngle))
                .rotateY((float) Math.toRadians(horizontalAngle));

        float speedFactor;

        if (Input.isHeldDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            speedFactor = 3f;
        } else {
            speedFactor = 1f;
        }

        if (Input.isHeldDown(GLFW.GLFW_KEY_D)) {
            component.transform.moveRel(new Vector3f(point.x, 0f, point.z).normalize().rotateY((float) Math.toRadians(90f)).mul(-deltaTime * speedFactor));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            component.transform.moveRel(new Vector3f(point.x, 0f, point.z).normalize().rotateY((float) Math.toRadians(90f)).mul(deltaTime * speedFactor));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            component.transform.moveRel(new Vector3f(point).normalize().mul(deltaTime * speedFactor));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_S)) {
            component.transform.moveRel(new Vector3f(point).normalize().mul(-deltaTime * speedFactor));
        }

        // todo: something wrong with projection when position point is not (0, 0, 0)
        //  needs to fix
        camera.viewMatrix = new Matrix4f()
                .identity()
                .lookAt(component.transform.position, new Vector3f(component.transform.position).add(point), new Vector3f(0, 1, 0));
        graphics.view = camera.viewMatrix;
    }

    private float restrictAngle(float angle, float lowest, float highest) {
        return Math.min(Math.max(angle, lowest), highest);
    }

    public static class WindowSizeCallback implements GLFWWindowSizeCallbackI {

        @Override
        public void invoke(long window, int width, int height) {
            camera.ratio = (float) width / height;
            CameraSystem.PERSPECTIVE_MATRIX  = new Matrix4f().perspective(camera.angle, camera.ratio, camera.near, camera.far);
            CameraSystem.ORTHOGRAPHIC_MATRIX = new Matrix4f().ortho(-camera.ratio, camera.ratio, -1, 1, -1, 1);

            Window windowObject = EngineConfig.instance.window;
            Graphics graphics = Graphics.getInstance(windowObject);

            if      (PERSPECTIVE_VIEW_STATE  == projectionState) graphics.projection = CameraSystem.PERSPECTIVE_MATRIX;
            else if (ORTHOGRAPHIC_VIEW_STATE == projectionState) graphics.projection = CameraSystem.ORTHOGRAPHIC_MATRIX;

            GL30.glViewport(0, 0, width, height);
            Logger.info(String.format("Aspect ratio: %f width: %s height: %s", camera.ratio, width, height));
        }
    }
}
