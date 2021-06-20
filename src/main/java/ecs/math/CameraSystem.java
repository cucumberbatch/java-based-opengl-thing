package ecs.math;

import ecs.Engine;
import ecs.entities.Entity;
import ecs.graphics.Shader;
import ecs.systems.AbstractECSSystem;
import ecs.systems.Input;

import static org.lwjgl.glfw.GLFW.*;

public class CameraSystem extends AbstractECSSystem<Camera> {

    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK;
    }

    @Override
    public void init() throws Exception {
        currentComponent.eye.set(0f, 0f, -1f);
        currentComponent.up.set(Vector3f.up());
        currentComponent.at.set(Vector3f.zero());
    }

    @Override
    public void update(float deltaTime) {
        Vector2f pos = Input.getCursorPosition();

        float xFactor = 1f;
        float yFactor = 1f;
        float zFactor = 1f;

//        currentComponent.at.set(Vector3f.add(currentComponent.eye, Vector3f.forward()));

        if (Input.isPressed(GLFW_KEY_G)) {
            Engine engine = currentComponent.entity.getEngine();
            Entity e = new Entity("generated entity", engine, engine.getScene());
            e.addComponent(Type.RIGIDBODY);
            engine.addEntity(e);
        }


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


        // test rotation around the pane or box
        float radius = 3f;
        currentComponent.lookAtMatrix = Matrix4f.lookAt(
                new Vector3f(
                        (float) (Math.sin(System.currentTimeMillis() * 0.005) * radius),
                        0f,
                        (float) (Math.cos(System.currentTimeMillis() * 0.005) * radius)),
                currentComponent.eye,
                Vector3f.up());

        // setting camera target view point around the camera position (XZ plane)
//        currentComponent.at.set(
//                Vector3f.add(currentComponent.eye, new Vector3f(cosx * cosx, sinx * sinx, 0f)));
//                Vector3f.add(currentComponent.eye, new Vector3f(1f, 0f, 0f)));

        // setting camera target view point around the camera position (YZ plane)
//        currentComponent.up.set(
//                Vector3f.add(currentComponent.eye, new Vector3f(0f, siny * siny, cosy * cosy)));
//                Vector3f.add(currentComponent.eye, new Vector3f(0f, 1f, cosy * cosy)));

        // calculating view matrix for camera rotation and position
//        currentComponent.lookAtMatrix = Matrix4f.lookAt(currentComponent.eye, currentComponent.at, currentComponent.up);

        currentComponent.viewMatrix = Matrix4f.multiply(
                currentComponent.lookAtMatrix,
                Matrix4f.translation(currentComponent.transform.position));

        // finally pass view matrix to shader
        Shader.BACKGROUND.setUniformMat4f("u_view", currentComponent.viewMatrix);

    }
}
