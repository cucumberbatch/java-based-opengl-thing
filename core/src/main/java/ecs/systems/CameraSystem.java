package ecs.systems;

import ecs.graphics.Shader;
import ecs.components.Camera;
import ecs.utils.Logger;
import matrices.Matrix4f;
import vectors.Vector2f;
import vectors.Vector3f;


public class CameraSystem extends AbstractECSSystem<Camera> {

    float near = 0.1f;
    float far = 100f;
    float angle = 180f;
    float ratio = 16f / 9f;

    Vector3f planePosition = new Vector3f(1f, 0f, -1f); //currentComponent.entity.getEngine().getEntityByName("side_plane").transform.position;
    


    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK;
    }

    @Override
    public void init() throws Exception {
        Logger.info(String.format("%s: started initialization..", this.getClass().getName()));
        component.eye.set(2f, 0f, -3f);
        component.up.set(Vector3f.up());
        component.at.set(Vector3f.add(component.eye, new Vector3f(1.0f, 0.0f, -1.0f)));
    }

    @Override
    public void update(float deltaTime) {
        Vector2f pos = Input.getCursorPosition();


        // precalculating trigonometry for circular camera rotation
        float sinx = (float) Math.sin(Math.toRadians(pos.x));
        float cosx = (float) Math.cos(Math.toRadians(pos.x));

        float siny = (float) Math.sin(Math.toRadians(pos.y));
        float cosy = (float) Math.cos(Math.toRadians(pos.y));

        // setting camera target view point around the camera position (XZ plane)
       // currentComponent.at.set(
       //         Vector3f.add(currentComponent.eye, new Vector3f(1f, 1f, 0f)));

        // setting camera target view point around the camera position (YZ plane)
       // currentComponent.up.set(
       //         Vector3f.add(currentComponent.eye, new Vector3f(0f, 1f, 1f)));

        // calculating view matrix for camera rotation and position
       component.lookAtMatrix = Matrix4f.lookAt(component.eye, component.at, component.up);

        component.viewMatrix = Matrix4f.multiply(
                component.lookAtMatrix,
                Matrix4f.translation(component.transform.position));

        // finally, pass view matrix to shader
        Shader.BACKGROUND.setUniformMat4f("u_view", component.lookAtMatrix);

        Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.orthographic(-5.0f, 5.0f, -5.0f, 5.0f, -5.0f, 5.0f));
       // Shader.BACKGROUND.setUniformMat4f("u_projection", Matrix4f.perspective(angle, near, far, ratio));
    }
}
