package org.north.core.system;

import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.component.Camera;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.RenderProcess;
import org.joml.Matrix4f;


@ComponentHandler(Camera.class)
public class CameraSystem extends AbstractSystem<Camera>
        implements InitProcess<Camera>, RenderProcess<Camera> {

    public static Matrix4f PERSPECTIVE_MATRIX = new Matrix4f();
    public static Matrix4f ORTHOGRAPHIC_MATRIX = new Matrix4f();

    @Inject
    public CameraSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(Camera camera) throws RuntimeException {
        PERSPECTIVE_MATRIX  = new Matrix4f().perspective(camera.angle, camera.ratio, camera.near, camera.far);
        ORTHOGRAPHIC_MATRIX = new Matrix4f().ortho(-1, 1, -1, 1, -1, 1);

        camera.eye.set(0f, -1f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.at.set(1.0f, 0.0f, -1.0f).add(camera.eye);
        camera.projectionMatrix = PERSPECTIVE_MATRIX;

        super.setCameraComponent(camera);
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
//        graphics.drawMesh(PredefinedMeshes.CUBE, new Vector4f(1, 1, 1, 1), component.transform);
    }

}
