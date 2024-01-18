package org.north.core.systems;

import org.north.core.graphics.Graphics;
import org.north.core.components.Camera;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.RenderProcess;
import org.joml.Matrix4f;
import org.joml.Vector3f;


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
        component.projectionMatrix = PERSPECTIVE_MATRIX;

        super.setCameraComponent(component);
    }

    @Override
    public void render(Graphics graphics) {
//        graphics.drawMesh(PredefinedMeshes.CUBE, new Vector4f(1, 1, 1, 1), component.transform);
    }

}
