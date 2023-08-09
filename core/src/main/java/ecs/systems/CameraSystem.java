package ecs.systems;

import ecs.graphics.Graphics;
import ecs.graphics.PredefinedMeshes;
import ecs.components.Camera;
import ecs.graphics.Window;
import ecs.reflection.ComponentHandler;
import ecs.utils.Logger;
import matrices.Matrix4f;
import org.lwjgl.glfw.GLFW;
import vectors.Vector2f;
import vectors.Vector3f;
import vectors.Vector4f;


@ComponentHandler(Camera.class)
public class CameraSystem extends AbstractSystem<Camera> {

    float near = 0.1f;
    float far = 100f;
    float angle = 180f;
    float ratio = 16f / 9f;

    Vector3f cameraPosition = new Vector3f();

//    public static Matrix4f projection = Matrix4f.perspective(90, -1f, 1f, 1);
//    public static Matrix4f projection = Matrix4f.perspective2(90, -1f, 1, 1);
//    public static Matrix4f projection = Matrix4f.orthographic(-1, 1, -1, 1, -1, 1);
//    public static Matrix4f projection = Matrix4f.orthographic2(-1, 1, -1, 1, -1, 1);
//    public static Matrix4f projection = Matrix4f.orthographic3(-1, 1, -1, 1, -1, 1);
//    public static Matrix4f projection = Matrix4f.perspective3(120, 1, -0.2f, 0.5f);

    private static final Matrix4f PERSPECTIVE_MATRIX  = Matrix4f.perspective3(90, 1, -0.2f, 0.5f);
    private static final Matrix4f ORTHOGRAPHIC_MATRIX = Matrix4f.orthographic3(-1, 1, -1, 1, -1, 1);

    private static final int PERSPECTIVE_VIEW_STATE = 0;
    private static final int PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE = 1;
    private static final int ORTHOGRAPHIC_VIEW_STATE = 2;
    private static final int ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE = 3;

    private int projectionState = ORTHOGRAPHIC_VIEW_STATE;
    private float projectionProgress = 0;

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
        component.projectionMatrix = ORTHOGRAPHIC_MATRIX;
    }

    @Override
    public void update(float deltaTime) {
        updateCameraMovement(deltaTime);
        updateCameraProjection();
    }

    private void updateCameraProjection() {
        switch (projectionState) {
            case PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE: {
                projectionProgress += 0.05;
                component.projectionMatrix = Matrix4f.lerp(PERSPECTIVE_MATRIX, ORTHOGRAPHIC_MATRIX, (float) (Math.sin(projectionProgress) / Math.sin(1)));
                if (projectionProgress > 1) {
                    projectionProgress = 0;
                    projectionState = ORTHOGRAPHIC_VIEW_STATE;
                }
                Graphics.setProjection(component.projectionMatrix);
                break;
            }
            case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE: {
                projectionProgress += 0.05;
                component.projectionMatrix = Matrix4f.lerp(ORTHOGRAPHIC_MATRIX, PERSPECTIVE_MATRIX, (float) (Math.sin(projectionProgress) / Math.sin(1))
                );
                if (projectionProgress > 1) {
                    projectionProgress = 0;
                    projectionState = PERSPECTIVE_VIEW_STATE;
                }
                Graphics.setProjection(component.projectionMatrix);
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
