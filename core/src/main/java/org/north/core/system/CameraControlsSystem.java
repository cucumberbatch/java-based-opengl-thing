package org.north.core.system;

import org.north.core.component.Camera;
import org.north.core.component.CameraControls;
import org.north.core.component.Transform;
import org.north.core.config.ApplicationProperties;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.RenderProcess;
import org.north.core.system.process.UpdateProcess;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL30;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayDeque;
import java.util.Queue;

import static java.lang.Math.PI;

@ComponentHandler(CameraControls.class)
public class CameraControlsSystem extends AbstractSystem<CameraControls>
        implements InitProcess<CameraControls>, UpdateProcess<CameraControls>, RenderProcess<CameraControls> {

    private static final float MAX_CAMERA_ANGLE = 89.986f;

    private static ProjectionState projectionState = ProjectionState.ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE;
    private static Camera camera;
    private final Graphics graphics;
    private final boolean intermediateCameraStateOnFocusLossEnabled;

    //todo implement camera trace effect
    private final Queue<Vector3f> cameraTrace = new ArrayDeque<>();

    private float projectionProgress = 0f;
    private float cameraMovementSpeed = 0f;
    private float cameraDragFactor = 0f;
    private float verticalMousePosition = 0f;

    private boolean mouseCaptured = false;

    public enum ProjectionState {
        PERSPECTIVE_VIEW_STATE,
        PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE,
        PERSPECTIVE_TO_INTERMEDIATE_VIEW_STATE,
        ORTHOGRAPHIC_VIEW_STATE,
        ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE,
        ORTHOGRAPHIC_TO_INTERMEDIATE_VIEW_STATE,
        INTERMEDIATE_VIEW_STATE,
        INTERMEDIATE_TO_PERSPECTIVE_VIEW_STATE,
        INTERMEDIATE_TO_ORTHOGRAPHIC_VIEW_STATE,
    }

    @Inject
    public CameraControlsSystem(ApplicationContext context) throws ReflectiveOperationException {
        super(context);
        this.graphics = context.getDependency(Graphics.class);
        this.intermediateCameraStateOnFocusLossEnabled =
                ApplicationProperties.getBoolean("application.editor.camera.intermediate-state-on-focus-loss");
    }

    @Override
    public void init(CameraControls cameraControls) {
        camera = cameraControls.entity.get(Camera.class);
    }

    @Override
    public void update(CameraControls cameraControls, float deltaTime) {
        updateScreenCapture(graphics);
        updateCameraProjection(graphics, deltaTime);
        updateCameraMovement(cameraControls, deltaTime);
    }

    @Override
    public void render(CameraControls cameraControls, Graphics graphics) {

    }

    private ProjectionState lastProjectionStateBeforeFocusLoss = ProjectionState.PERSPECTIVE_VIEW_STATE;

    private void updateScreenCapture(Graphics graphics) {
        if (Input.isPressed(GLFW.GLFW_KEY_ESCAPE)) {
            GLFW.glfwSetInputMode(graphics.window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

            if (intermediateCameraStateOnFocusLossEnabled) {
                switch (projectionState) {
                    case INTERMEDIATE_TO_PERSPECTIVE_VIEW_STATE:
                    case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE:
                    case PERSPECTIVE_VIEW_STATE:
                        projectionState = ProjectionState.PERSPECTIVE_TO_INTERMEDIATE_VIEW_STATE;
                        projectionProgress = 0f;
                        lastProjectionStateBeforeFocusLoss = ProjectionState.PERSPECTIVE_VIEW_STATE;
                        break;
                    case INTERMEDIATE_TO_ORTHOGRAPHIC_VIEW_STATE:
                    case PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE:
                    case ORTHOGRAPHIC_VIEW_STATE:
                        projectionState = ProjectionState.ORTHOGRAPHIC_TO_INTERMEDIATE_VIEW_STATE;
                        projectionProgress = 0f;
                        lastProjectionStateBeforeFocusLoss = ProjectionState.ORTHOGRAPHIC_VIEW_STATE;
                        break;
                }
            }
            mouseCaptured = false;
        }

        if (Input.isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !mouseCaptured) {
            GLFW.glfwSetInputMode(graphics.window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

            if (intermediateCameraStateOnFocusLossEnabled) {
                switch (lastProjectionStateBeforeFocusLoss) {
                    case PERSPECTIVE_TO_INTERMEDIATE_VIEW_STATE:
                    case PERSPECTIVE_VIEW_STATE:
                        projectionState = ProjectionState.INTERMEDIATE_TO_PERSPECTIVE_VIEW_STATE;
                        projectionProgress = 0f;
                        break;
                    case ORTHOGRAPHIC_TO_INTERMEDIATE_VIEW_STATE:
                    case ORTHOGRAPHIC_VIEW_STATE:
                        projectionState = ProjectionState.INTERMEDIATE_TO_ORTHOGRAPHIC_VIEW_STATE;
                        projectionProgress = 0f;
                        break;
                }
            }

            previousCursorPosition = Input.getCursorPosition();
            cursorPositionDifference = new Vector2f();
            mouseCaptured = true;
        }
    }

    private final float PROJECTION_PROGRESS_STEP = 2f;
    private final double HALF_PI = PI / 2;
    private final double SIN_OF_HALF_PI = Math.sin(HALF_PI);

    private void updateCameraProjection(Graphics graphics, float deltaTime) {
        if (Input.isPressed(GLFW.GLFW_KEY_1)) {
            switch (projectionState) {
                case ORTHOGRAPHIC_VIEW_STATE:
                case INTERMEDIATE_TO_ORTHOGRAPHIC_VIEW_STATE:
                case PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE:
                    projectionState = ProjectionState.ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE;
                    projectionProgress = 0f;
                    break;

                case PERSPECTIVE_VIEW_STATE:
                case INTERMEDIATE_TO_PERSPECTIVE_VIEW_STATE:
                case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE:
                    projectionState = ProjectionState.PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE;
                    projectionProgress = 0f;
                    break;
            }
        }

        switch (projectionState) {
            case INTERMEDIATE_TO_ORTHOGRAPHIC_VIEW_STATE:
            case PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE: {
                projectionProgress += PROJECTION_PROGRESS_STEP * deltaTime;
                camera.projectionMatrix = new Matrix4f(camera.projectionMatrix).lerp(CameraSystem.ORTHOGRAPHIC_MATRIX, (float) (Math.sin(projectionProgress) / SIN_OF_HALF_PI));
                if (projectionProgress > HALF_PI) {
                    projectionProgress = 0;
                    projectionState = ProjectionState.ORTHOGRAPHIC_VIEW_STATE;
                }
                break;
            }
            case INTERMEDIATE_TO_PERSPECTIVE_VIEW_STATE:
            case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE: {
                projectionProgress += PROJECTION_PROGRESS_STEP * deltaTime;
                camera.projectionMatrix = new Matrix4f(camera.projectionMatrix).lerp(CameraSystem.PERSPECTIVE_MATRIX, (float) (Math.sin(projectionProgress) / SIN_OF_HALF_PI));
                if (projectionProgress > HALF_PI) {
                    projectionProgress = 0;
                    projectionState = ProjectionState.PERSPECTIVE_VIEW_STATE;
                }
                break;
            }
            case ORTHOGRAPHIC_TO_INTERMEDIATE_VIEW_STATE:
            case PERSPECTIVE_TO_INTERMEDIATE_VIEW_STATE: {
                projectionProgress += PROJECTION_PROGRESS_STEP * deltaTime;
                camera.projectionMatrix = new Matrix4f(camera.projectionMatrix).lerp(CameraSystem.INTERMEDIATE_MATRIX, (float) (Math.sin(projectionProgress) / SIN_OF_HALF_PI));
                if (projectionProgress > HALF_PI) {
                    projectionProgress = 0;
                    projectionState = ProjectionState.INTERMEDIATE_VIEW_STATE;
                }
                break;

            }
        }
        graphics.projection = camera.projectionMatrix;
    }

    private Vector2f virtualCursorPosition = new Vector2f();
    private Vector2f previousCursorPosition = new Vector2f();
    private Vector2f cursorPositionDifference = new Vector2f();


    private void updateCameraMovement(CameraControls cameraControls, float deltaTime) {
        if (!mouseCaptured) return;

        Vector3f temp = vector3fPool.get();
        Vector3f temp2 = vector3fPool.get();

        Vector2f cursorPosition = Input.getCursorPosition();
        cursorPositionDifference = new Vector2f(cursorPosition).sub(previousCursorPosition);
        virtualCursorPosition = new Vector2f(virtualCursorPosition).add(cursorPositionDifference);
        previousCursorPosition = cursorPosition;

        float verticalAngle = virtualCursorPosition.y / (graphics.window.getWidth() / 256f) - 180;
        float horizontalAngle = -virtualCursorPosition.x / (graphics.window.getWidth() / 256f) - 180;

        verticalAngle = restrictAngle(verticalAngle, -MAX_CAMERA_ANGLE, MAX_CAMERA_ANGLE);



        Vector3f point = temp.set(0f, 0f, 1f)
                .rotateX((float) Math.toRadians(verticalAngle))
                .rotateY((float) Math.toRadians(horizontalAngle));

        cameraMovementSpeed = Input.isHeldDown(GLFW.GLFW_KEY_LEFT_SHIFT) ? cameraMovementSpeed + deltaTime * 1.3f : 1f;

        Transform componentTransform = cameraControls.getTransform();

        // up-down movement
        // note: incorrect
        if (Input.isHeldDown(GLFW.GLFW_KEY_Q)) {
            componentTransform.moveRel(temp2.set(point).normalize().rotateX((float) Math.toRadians(90f)).mul(-deltaTime * cameraMovementSpeed));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_E)) {
            componentTransform.moveRel(temp2.set(point).normalize().rotateX((float) Math.toRadians(90f)).mul(deltaTime * cameraMovementSpeed));
        }

        // left-right movement
        if (Input.isHeldDown(GLFW.GLFW_KEY_D)) {
            componentTransform.moveRel(temp2.set(point.x, 0f, point.z).normalize().rotateY((float) Math.toRadians(90f)).mul(-deltaTime * cameraMovementSpeed));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            componentTransform.moveRel(temp2.set(point.x, 0f, point.z).normalize().rotateY((float) Math.toRadians(90f)).mul(deltaTime * cameraMovementSpeed));
        }

        // forward-backward movement
        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            componentTransform.moveRel(temp2.set(point).normalize().mul(deltaTime * cameraMovementSpeed));
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_S)) {
            componentTransform.moveRel(temp2.set(point).normalize().mul(-deltaTime * cameraMovementSpeed));
        }

        // todo: something wrong with projection when position point is not (0, 0, 0)
        //  needs to fix

        camera.eye.set(componentTransform.position);
        camera.at.set(point.add(componentTransform.position));

        camera.viewMatrix.identity().lookAt(camera.eye, camera.at, temp2.set(0f, 1f, 0f));

        vector3fPool.put(temp2);
        vector3fPool.put(temp);

        graphics.view = camera.viewMatrix;
    }

    private float restrictAngle(float angle, float lowest, float highest) {
        return Math.min(Math.max(angle, lowest), highest);
    }

    public static class WindowSizeCallback implements GLFWWindowSizeCallbackI {
        private final Graphics graphics;

        public WindowSizeCallback(Graphics graphics) {
            this.graphics = graphics;
        }

        @Override
        public void invoke(long window, int width, int height) {
            camera.ratio = (float) width / height;
            CameraSystem.PERSPECTIVE_MATRIX = new Matrix4f().perspective(camera.angle, camera.ratio, camera.near, camera.far);
            CameraSystem.ORTHOGRAPHIC_MATRIX = new Matrix4f().ortho(-camera.ratio, camera.ratio, -1, 1, -1, 1);

            if (ProjectionState.PERSPECTIVE_VIEW_STATE == projectionState) graphics.projection = CameraSystem.PERSPECTIVE_MATRIX;
            else if (ProjectionState.ORTHOGRAPHIC_VIEW_STATE == projectionState) graphics.projection = CameraSystem.ORTHOGRAPHIC_MATRIX;

            GL30.glViewport(0, 0, width, height);
            java.lang.System.out.printf("Aspect ratio: %f width: %s height: %s\n", camera.ratio, width, height);
        }
    }
}
