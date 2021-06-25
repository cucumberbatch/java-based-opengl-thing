package ecs.systems;

import ecs.Engine;
import ecs.components.Camera;
import ecs.entities.Entity;
import ecs.graphics.Shader;
import ecs.math.Matrix4f;
import ecs.math.Vector2f;
import ecs.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class CameraSystem extends AbstractECSSystem<Camera> {

    public CameraSystem() {
        setWorkflowMask(INIT_MASK | UPDATE_MASK);
    }

    @Override
    public void onInit() {
        component.eye.set(0f, 0f, -1f);
        component.up.set(Vector3f.up());
        component.at.set(Vector3f.zero());
    }

    @Override
    public void onUpdate(float deltaTime) {
        Vector2f pos = Input.getCursorPosition();

        float xFactor = 1f;
        float yFactor = 1f;
        float zFactor = 1f;

//        currentComponent.at.set(Vector3f.add(currentComponent.eye, Vector3f.forward()));

        if (Input.isPressed(GLFW_KEY_G)) {
            Engine engine = component.entity.getEngine();
            Entity e = new Entity("generated entity", engine, engine.getScene());
            e.addComponent(Type.PLANE);
            engine.addEntity(e);
        }


        if (Input.isHeldDown(GLFW_KEY_W)) {
            component.eye.add(0f, 0f, zFactor * deltaTime);
        }
        if (Input.isHeldDown(GLFW_KEY_S)) {
            component.eye.add(0f, 0f, -zFactor * deltaTime);
        }
        if (Input.isHeldDown(GLFW_KEY_A)) {
            component.eye.add(xFactor * deltaTime, 0f, 0f);
        }
        if (Input.isHeldDown(GLFW_KEY_D)) {
            component.eye.add(-xFactor * deltaTime, 0f, 0f);
        }

        // precalculating trigonometry for circular camera rotation
        float sinx = (float) Math.sin(Math.toRadians(pos.x));
        float cosx = (float) Math.cos(Math.toRadians(pos.x));

        float siny = (float) Math.sin(Math.toRadians(pos.y));
        float cosy = (float) Math.cos(Math.toRadians(pos.y));


        // test rotation around the pane or box
        float radius = 3f;
        component.lookAtMatrix = Matrix4f.lookAt(
                new Vector3f(
                        (float) (Math.sin(System.currentTimeMillis() * 0.005) * radius),
                        0f,
                        (float) (Math.cos(System.currentTimeMillis() * 0.005) * radius)),
                component.eye,
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

        component.viewMatrix = Matrix4f.multiply(
                component.lookAtMatrix,
                Matrix4f.translation(component.transform.position));

        // finally pass view matrix to shader
        Shader.BACKGROUND.setUniformMat4f("u_view", component.viewMatrix);

    }
}
