package ecs.systems;

import ecs.graphics.Graphics;
import ecs.graphics.PredefinedMeshes;
import ecs.components.Camera;
import ecs.graphics.Window;
import ecs.reflection.ComponentHandler;
import ecs.utils.Logger;
import matrices.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL30;
import vectors.Vector2f;
import vectors.Vector3f;
import vectors.Vector4f;


@ComponentHandler(Camera.class)
public class CameraSystem extends AbstractSystem<Camera> {

    Vector3f cameraPosition = new Vector3f();

//    public static Matrix4f projection = Matrix4f.perspective(90, -1f, 1f, 1);
//    public static Matrix4f projection = Matrix4f.perspective2(90, -1f, 1, 1);
//    public static Matrix4f projection = Matrix4f.orthographic(-1, 1, -1, 1, -1, 1);
//    public static Matrix4f projection = Matrix4f.orthographic2(-1, 1, -1, 1, -1, 1);
//    public static Matrix4f projection = Matrix4f.orthographic3(-1, 1, -1, 1, -1, 1);
//    public static Matrix4f projection = Matrix4f.perspective3(120, 1, -0.2f, 0.5f);

    public static Matrix4f PERSPECTIVE_MATRIX;
    public static Matrix4f ORTHOGRAPHIC_MATRIX;




    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK | RENDER_MASK;
    }

    @Override
    public void init() throws RuntimeException {
        Logger.info(String.format("%s: started initialization..", this.getClass().getName()));

        PERSPECTIVE_MATRIX  = Matrix4f.perspective3(component.angle, component.ratio, component.near, component.far);
        ORTHOGRAPHIC_MATRIX = Matrix4f.orthographic3(-1, 1, -1, 1, -1, 1);

        component.eye.set(2f, 0f, -3f);
        component.up.set(Vector3f.up());
        component.at.set(new Vector3f(1.0f, 0.0f, -1.0f).add(component.eye));
        component.projectionMatrix = ORTHOGRAPHIC_MATRIX;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawMesh(PredefinedMeshes.CUBE, Vector4f.one(), component.transform);
    }

}
