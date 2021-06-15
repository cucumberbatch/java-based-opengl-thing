package ecs.math;

import ecs.graphics.Shader;
import ecs.systems.AbstractECSSystem;
import ecs.systems.Input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public class CameraSystem extends AbstractECSSystem<Camera> {
    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK;
    }

    @Override
    public void init() throws Exception {
        currentComponent.eye.set(0f, 0f, 0f);
        currentComponent.up.set(Vector3f.up());
    }

    @Override
    public void update(float deltaTime) {
        Vector2f pos = Input.getCursorPosition();

        float xFactor = 1f;
        float yFactor = 1f;
        float zFactor = 1f;


        if (Input.isHeldDown(GLFW_KEY_W)) {
            currentComponent.eye.add(0f, 0f, zFactor * deltaTime);
        }
        if (Input.isHeldDown(GLFW_KEY_S)) {
            currentComponent.eye.add(0f, 0f, -zFactor * deltaTime);
        }
        if (Input.isHeldDown(GLFW_KEY_A)) {
            currentComponent.eye.add(xFactor * deltaTime, 0f, 0f);
        }
        if (Input.isHeldDown(GLFW_KEY_D)) {
            currentComponent.eye.add(-xFactor * deltaTime, 0f, 0f);
        }

        // precalculating trigonometry for circular camera rotation
        float sinx = (float) Math.sin(Math.toRadians(pos.x));
        float cosx = (float) Math.cos(Math.toRadians(pos.x));

        float siny = (float) Math.sin(Math.toRadians(pos.y));
        float cosy = (float) Math.cos(Math.toRadians(pos.y));

        // setting camera target view point around the camera position (XZ plane)
        currentComponent.at.set(
//                Vector3f.add(currentComponent.eye, new Vector3f(cosx * cosx, sinx * sinx, 0f)));
                Vector3f.add(currentComponent.eye, new Vector3f(1f, 0f, 0f)));

        // setting camera target view point around the camera position (YZ plane)
        currentComponent.up.set(
//                Vector3f.add(currentComponent.eye, new Vector3f(0f, siny * siny, cosy * cosy)));
                Vector3f.add(currentComponent.eye, new Vector3f(0f, 1f, cosy * cosy)));

        // calculating view matrix for camera rotation and position
        currentComponent.lookAtMatrix = Matrix4f.lookAt(currentComponent.eye, currentComponent.at, currentComponent.up);
        currentComponent.viewMatrix   = Matrix4f.multiply(
                currentComponent.lookAtMatrix,
                Matrix4f.translation(currentComponent.transform.position));

        // finally pass view matrix to shader
        Shader.BACKGROUND.setUniformMat4f("u_view", currentComponent.viewMatrix);

    }
}
