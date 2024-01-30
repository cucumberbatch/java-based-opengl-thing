package org.north.core.systems;

import org.north.core.components.Camera;
import org.north.core.components.CameraControls;
import org.north.core.components.Transform;
import org.north.core.config.EngineConfig;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.UpdateProcess;
import org.north.core.utils.Logger;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL30;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayDeque;
import java.util.Queue;

@ComponentHandler(CameraControls.class)
public class CameraControlsSystem extends AbstractSystem<CameraControls> implements InitProcess, UpdateProcess {

    private static final int PERSPECTIVE_VIEW_STATE = 0;
    private static final int PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE = 1;
    private static final int ORTHOGRAPHIC_VIEW_STATE = 2;
    private static final int ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE = 3;

    private static final float MAX_CAMERA_ANGLE = 89.986f;

    private static int projectionState = ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE;
    private static Camera camera;
    private final Queue<Vector3f> cameraTrace = new ArrayDeque<>();
    private float projectionProgress = 0;
    private Graphics graphics;
    private float cameraMovementSpeed;
    private Vector2f mousePosition = new Vector2f();
    private float verticalMousePosition = 0;
    private boolean mouseCaptured = false;


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
//            Logger.info("ESC: " + graphics.view.toString());
            mouseCaptured = false;
        }

        if (Input.isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            GLFW.glfwSetInputMode(graphics.window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
//            Logger.info("CLICK: " + graphics.view.toString());
            mouseCaptured = true;
        }
    }

    private void updateCameraProjection() {
        switch (projectionState) {
            case PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE: {
                projectionProgress += 0.05f;
                camera.projectionMatrix = new Matrix4f(CameraSystem.PERSPECTIVE_MATRIX).lerp(CameraSystem.ORTHOGRAPHIC_MATRIX, (float) (Math.sin(projectionProgress) / Math.sin(1)));
                if (projectionProgress > 1) {
                    projectionProgress = 0;
                    projectionState = ORTHOGRAPHIC_VIEW_STATE;
                }
                graphics.projection = camera.projectionMatrix;
                break;
            }
            case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE: {
                projectionProgress += 0.05f;
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
        if (!mouseCaptured) return;

        Vector3f temp = vector3IPool.get();
        Vector3f temp2 = vector3IPool.get();

        Vector2f cursorPosition = Input.getCursorPosition();

        float verticalAngle = cursorPosition.y / (Window.width / 256f) - 180;
        float horizontalAngle = -cursorPosition.x / (Window.width / 256f) - 180;

        verticalAngle = restrictAngle(verticalAngle, -MAX_CAMERA_ANGLE, MAX_CAMERA_ANGLE);



        Vector3f point = temp.set(0f, 0f, 1f)
                .rotateX((float) Math.toRadians(verticalAngle))
                .rotateY((float) Math.toRadians(horizontalAngle));

        if (Input.isHeldDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            cameraMovementSpeed = cameraMovementSpeed + deltaTime * 1.3f;
        } else {
            cameraMovementSpeed = 1f;
        }

        Transform componentTransform = component.getTransform();

        // up-down movement
        // note: incorrect
        if (Input.isHeldDown(GLFW.GLFW_KEY_Q)) {
            componentTransform.moveRel(temp2.set(point).normalize().rotateX((float) Math.toRadians(90f)).mul(-deltaTime * cameraMovementSpeed));
//            cameraTrace.add(componentTransform.position);
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_E)) {
            componentTransform.moveRel(temp2.set(point).normalize().rotateX((float) Math.toRadians(90f)).mul(deltaTime * cameraMovementSpeed));
//            cameraTrace.add(componentTransform.position);
        }

        // left-right movement
        if (Input.isHeldDown(GLFW.GLFW_KEY_D)) {
            componentTransform.moveRel(temp2.set(point.x, 0f, point.z).normalize().rotateY((float) Math.toRadians(90f)).mul(-deltaTime * cameraMovementSpeed));
//            cameraTrace.add(componentTransform.position);
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            componentTransform.moveRel(temp2.set(point.x, 0f, point.z).normalize().rotateY((float) Math.toRadians(90f)).mul(deltaTime * cameraMovementSpeed));
//            cameraTrace.add(componentTransform.position);
        }

        // forward-backward movement
        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            componentTransform.moveRel(temp2.set(point).normalize().mul(deltaTime * cameraMovementSpeed));
//            cameraTrace.add(componentTransform.position);
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_S)) {
            componentTransform.moveRel(temp2.set(point).normalize().mul(-deltaTime * cameraMovementSpeed));
//            cameraTrace.add(componentTransform.position);
        }

        // todo: something wrong with projection when position point is not (0, 0, 0)
        //  needs to fix
        camera.viewMatrix.identity().lookAt(componentTransform.position, point.add(componentTransform.position), temp2.set(0f, 1f, 0f));
        graphics.view = camera.viewMatrix;

        vector3IPool.put(temp2);
        vector3IPool.put(temp);
    }

    private float restrictAngle(float angle, float lowest, float highest) {
        return Math.min(Math.max(angle, lowest), highest);
    }

    public static class WindowSizeCallback implements GLFWWindowSizeCallbackI {

        @Override
        public void invoke(long window, int width, int height) {
            camera.ratio = (float) width / height;
            CameraSystem.PERSPECTIVE_MATRIX = new Matrix4f().perspective(camera.angle, camera.ratio, camera.near, camera.far);
            CameraSystem.ORTHOGRAPHIC_MATRIX = new Matrix4f().ortho(-camera.ratio, camera.ratio, -1, 1, -1, 1);

            Window windowObject = EngineConfig.instance.window;
            Graphics graphics = Graphics.getInstance(windowObject);

            if (PERSPECTIVE_VIEW_STATE == projectionState) graphics.projection = CameraSystem.PERSPECTIVE_MATRIX;
            else if (ORTHOGRAPHIC_VIEW_STATE == projectionState) graphics.projection = CameraSystem.ORTHOGRAPHIC_MATRIX;

            GL30.glViewport(0, 0, width, height);
            // Logger.info(String.format("Aspect ratio: %f width: %s height: %s", camera.ratio, width, height));
        }
    }
}
