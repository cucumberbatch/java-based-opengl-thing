package ecs.systems;

import ecs.graphics.Graphics;
import ecs.graphics.PredefinedMeshes;
import ecs.components.Camera;
import ecs.reflection.ComponentHandler;
import ecs.systems.processes.InitProcess;
import ecs.systems.processes.RenderProcess;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;


@ComponentHandler(Camera.class)
public class CameraSystem extends AbstractSystem<Camera>
        implements InitProcess, RenderProcess {

    public static Matrix4f PERSPECTIVE_MATRIX = new Matrix4f();
    public static Matrix4f ORTHOGRAPHIC_MATRIX = new Matrix4f();

    @Override
    public void init() throws RuntimeException {
        PERSPECTIVE_MATRIX  = new Matrix4f().perspective(component.angle, component.ratio, component.near, component.far);
        ORTHOGRAPHIC_MATRIX = new Matrix4f().ortho(-1, 1, -1, 1, -1, 1);

        component.eye.set(0f, -1f, 0f);
        component.up.set(new Vector3f(0, 1, 0));
        component.at.set(new Vector3f(1.0f, 0.0f, -1.0f).add(component.eye));
        component.projectionMatrix = ORTHOGRAPHIC_MATRIX;
    }

    @Override
    public void render(Graphics graphics) {
//        graphics.drawMesh(PredefinedMeshes.CUBE, new Vector4f(1, 1, 1, 1), component.transform);
    }

}
