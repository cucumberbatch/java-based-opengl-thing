package ecs.systems;

import ecs.components.Camera;
import ecs.components.CameraControls;
import ecs.graphics.Graphics;
import ecs.graphics.Window;
import ecs.reflection.ComponentHandler;
import ecs.utils.Logger;
import matrices.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL30;
import vectors.Vector2f;
import vectors.Vector3f;

@ComponentHandler(CameraControls.class)
public class CameraControlsSystem extends AbstractSystem<CameraControls> {

    private static final int PERSPECTIVE_VIEW_STATE = 0;
    private static final int PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE = 1;
    private static final int ORTHOGRAPHIC_VIEW_STATE = 2;
    private static final int ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE = 3;

    private static int projectionState = ORTHOGRAPHIC_VIEW_STATE;
    private float projectionProgress = 0;

    private static Camera camera;

    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK | RENDER_MASK;
    }

    @Override
    public void init() {
        camera = component.entity.getComponent(Camera.class);
    }

    @Override
    public void update(float deltaTime) {
        updateCameraMovement(deltaTime);
        updateCameraProjection();
    }

    @Override
    public void render(Graphics graphics) {
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
                camera.projectionMatrix = Matrix4f.lerp(CameraSystem.PERSPECTIVE_MATRIX, CameraSystem.ORTHOGRAPHIC_MATRIX, (float) (Math.sin(projectionProgress) / Math.sin(1)));
                if (projectionProgress > 1) {
                    projectionProgress = 0;
                    projectionState = ORTHOGRAPHIC_VIEW_STATE;
                }
                Graphics.setProjection(camera.projectionMatrix);
                break;
            }
            case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE: {
                projectionProgress += 0.05;
                camera.projectionMatrix = Matrix4f.lerp(CameraSystem.ORTHOGRAPHIC_MATRIX, CameraSystem.PERSPECTIVE_MATRIX, (float) (Math.sin(projectionProgress) / Math.sin(1))
                );
                if (projectionProgress > 1) {
                    projectionProgress = 0;
                    projectionState = PERSPECTIVE_VIEW_STATE;
                }
                Graphics.setProjection(camera.projectionMatrix);
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

        float verticalAngle   = -cursorPosition.y / (Window.width / 256f) - 180;
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
            component.transform.moveRel(new Vector3f(deltaTime * speedFactor, 0, 0));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            component.transform.moveRel(new Vector3f(-deltaTime * speedFactor, 0, 0));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            component.transform.moveRel(new Vector3f(0, deltaTime * speedFactor, 0));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_S)) {
            component.transform.moveRel(new Vector3f(0, -deltaTime * speedFactor, 0));
        }

        // todo: something wrong with projection when position point is not (0, 0, 0)
        //  needs to fix
        camera.viewMatrix = Matrix4f.lookAt(new Vector3f(), point, Vector3f.up());

        Graphics.setView(camera.viewMatrix);
    }

    private float restrictAngle(float angle, float lowest, float highest) {
        return Math.min(Math.max(angle, lowest), highest);
    }

    public static class WindowSizeCallback implements GLFWWindowSizeCallbackI {

        @Override
        public void invoke(long window, int width, int height) {
            camera.ratio = (float) width / height;
            CameraSystem.PERSPECTIVE_MATRIX  = Matrix4f.perspective3(camera.angle, camera.ratio, camera.near, camera.far);
            CameraSystem.ORTHOGRAPHIC_MATRIX = Matrix4f.orthographic3(-camera.ratio, camera.ratio, -1, 1, -1, 1);

            if      (PERSPECTIVE_VIEW_STATE  == projectionState) Graphics.setProjection(CameraSystem.PERSPECTIVE_MATRIX);
            else if (ORTHOGRAPHIC_VIEW_STATE == projectionState) Graphics.setProjection(CameraSystem.ORTHOGRAPHIC_MATRIX);

            GL30.glViewport(0, 0, width, height);
            Logger.info(String.format("Aspect ratio: %f width: %s height: %s", camera.ratio, width, height));
        }
    }
}
