package org.north.core.system;

import org.north.core.component.Camera;
import org.north.core.component.CameraControls;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
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

    private static final int PERSPECTIVE_VIEW_STATE = 0;
    private static final int PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE = 1;
    private static final int ORTHOGRAPHIC_VIEW_STATE = 2;
    private static final int ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE = 3;

    private static final float MAX_CAMERA_ANGLE = 89.986f;

    private static int projectionState = ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE;
    private static Camera camera;
    private final Graphics graphics;
    private final Window window;

    private final Queue<Vector3f> cameraTrace = new ArrayDeque<>();
    private float projectionProgress = 0;
    private float cameraMovementSpeed;
    private Vector2f mousePosition = new Vector2f();
    private float verticalMousePosition = 0;
    private boolean mouseCaptured = false;

    @Inject
    public CameraControlsSystem(ApplicationContext context) throws ReflectiveOperationException {
        super(context);
        this.graphics = context.getDependency(Graphics.class);
        this.window = context.getDependency(Window.class);
    }

    @Override
    public void init(CameraControls cameraControls) {
        camera = cameraControls.entity.get(Camera.class);
    }

    @Override
    public void update(CameraControls cameraControls, float deltaTime) {
        updateCameraMovement(cameraControls, deltaTime);
        updateCameraProjection();
        updateScreenCapture(graphics);
//
//        if (!mouseCaptured) {
//            Vector2f cursorPosition = Input.getCursorPosition();
//            Vector3f rayOrigin = camera.eye;
//            Vector3f rayDirection = camera.at;
//
//
//            Entity cube = et.getByName("right");
//            cube.transform.moveTo(rayDirection.mul(2, new Vector3f()));
//        }
//    }
//
//    @Override
//    public void render(CameraControls cameraControls, Graphics graphics) {

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

    private static final float PROJECTION_PROGRESS_STEP = 0.06f;
    private static final double HALF_PI = PI / 2;
    private static final double SIN_OF_HALF_PI = Math.sin(HALF_PI);

    private void updateCameraProjection() {
        switch (projectionState) {
            case PERSPECTIVE_TO_ORTHOGRAPHIC_VIEW_STATE: {
                projectionProgress += PROJECTION_PROGRESS_STEP;
                camera.projectionMatrix = new Matrix4f(CameraSystem.PERSPECTIVE_MATRIX).lerp(CameraSystem.ORTHOGRAPHIC_MATRIX, (float) (Math.sin(projectionProgress) / SIN_OF_HALF_PI));
                if (projectionProgress > HALF_PI) {
                    projectionProgress = 0;
                    projectionState = ORTHOGRAPHIC_VIEW_STATE;
                }
                graphics.projection = camera.projectionMatrix;
                break;
            }
            case ORTHOGRAPHIC_TO_PERSPECTIVE_VIEW_STATE: {
                projectionProgress += PROJECTION_PROGRESS_STEP;
                camera.projectionMatrix = new Matrix4f(CameraSystem.ORTHOGRAPHIC_MATRIX).lerp(CameraSystem.PERSPECTIVE_MATRIX, (float) (Math.sin(projectionProgress) / SIN_OF_HALF_PI));
                if (projectionProgress > HALF_PI) {
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

    private void updateCameraMovement(CameraControls cameraControls, float deltaTime) {
        if (!mouseCaptured) return;

        Vector3f temp = vector3fPool.get();
        Vector3f temp2 = vector3fPool.get();

        Vector2f cursorPosition = Input.getCursorPosition();

//        java.lang.System.out.println(cursorPosition);

        float verticalAngle = cursorPosition.y / (window.getWidth() / 256f) - 180;
        float horizontalAngle = -cursorPosition.x / (window.getWidth() / 256f) - 180;

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

        camera.eye.set(componentTransform.position);
        camera.at.set(point.add(componentTransform.position));

        camera.viewMatrix.identity().lookAt(camera.eye, camera.at, temp2.set(0f, 1f, 0f));
        graphics.view = camera.viewMatrix;

        vector3fPool.put(temp2);
        vector3fPool.put(temp);
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

            if (PERSPECTIVE_VIEW_STATE == projectionState) graphics.projection = CameraSystem.PERSPECTIVE_MATRIX;
            else if (ORTHOGRAPHIC_VIEW_STATE == projectionState) graphics.projection = CameraSystem.ORTHOGRAPHIC_MATRIX;

            GL30.glViewport(0, 0, width, height);
            // Logger.info(String.format("Aspect ratio: %f width: %s height: %s", camera.ratio, width, height));
        }
    }
}
