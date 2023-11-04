package ecs.systems;

import ecs.scene.Scene;
import ecs.architecture.ComponentManager;
import ecs.architecture.entity.ImprovedEntityManager;
import ecs.components.*;
import ecs.config.EngineConfig;
import ecs.entities.Entity;
import ecs.graphics.Mesh;
import ecs.graphics.PredefinedMeshes;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.reflection.ComponentHandler;
import ecs.systems.processes.InitProcess;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

@ComponentHandler(InitEntities.class)
public class InitEntitiesSystem extends AbstractSystem<InitEntities>
        implements InitProcess {

    private final ImprovedEntityManager em = new ImprovedEntityManager(ComponentManager.getInstance());

    @Override
    public void init() throws RuntimeException {


//        initSandbox();
        initCubeAndCamera();
    }

    private void initSandbox() {
        em.take(em.create("sandBox"))
                .add(Transform.class, Sandbox.class);
    }

    private void initCubeAndCamera() {
        List<Component> components;

        em.take(em.create("camera"))
                .add(Transform.class, Camera.class, CameraControls.class);


        components = em.take(em.create("cube")).add(Transform.class, MeshRenderer.class);

        ((MeshRenderer) components.get(1)).mesh = PredefinedMeshes.CUBE;


        components = em.take(em.create("north")).add(Transform.class, MeshRenderer.class);

        ((Transform) components.get(0)).moveTo(0f, 5f, 0f);
        ((Transform) components.get(0)).scale = new Vector3f(0.2f, 0.2f, 0.2f);
        ((MeshRenderer) components.get(1)).mesh = PredefinedMeshes.CUBE;
        ((MeshRenderer) components.get(1)).color = new Vector4f(0, 0, 1, 1);
        ((MeshRenderer) components.get(1)).renderType = GL11.GL_TRIANGLES;


        components = em.take(em.create("south")).add(Transform.class, MeshRenderer.class);

        ((Transform) components.get(0)).moveTo(0f, -5f, 0f);
        ((Transform) components.get(0)).rotation = new Vector3f(0f, -5f, 0f);
        ((Transform) components.get(0)).scale = new Vector3f(0.2f, 0.2f, 0.2f);
        ((MeshRenderer) components.get(1)).mesh = PredefinedMeshes.CUBE;
        ((MeshRenderer) components.get(1)).color = new Vector4f(1, 0, 0, 1);
        ((MeshRenderer) components.get(1)).texture = new Texture("core/assets/textures/screen-frame-1024.png");
        ((MeshRenderer) components.get(1)).renderType = GL11.GL_TRIANGLES;

    }

    private void initSingleButtonScene() {
        Scene scene = new Scene();

        Entity button = new Entity("button");

        scene.addEntity(button);

        Transform transformComponent = new Transform();
        Button buttonComponent = new Button();
        MeshRenderer rendererComponent = new MeshRenderer();

        int height = EngineConfig.instance.windowHeight;
        int width = EngineConfig.instance.windowWidth;

        transformComponent.position.set(1, 0, 1);

        rendererComponent.mesh = new Mesh();
        rendererComponent.shader = Shader.GUI;
        rendererComponent.texture = new Texture("core/assets/textures/screen-frame-1024.png");

        button.addComponent(transformComponent);
        button.addComponent(buttonComponent);
        button.addComponent(rendererComponent);

        transformComponent.entity = button;
        buttonComponent.entity = button;
        rendererComponent.entity = button;

        componentManager.addComponent(button, transformComponent);
        componentManager.addComponent(button, buttonComponent);
        componentManager.addComponent(button, rendererComponent);
    }

    private void initScene1() {
        Scene scene = new Scene("scene name");

        Entity cursor       = new Entity("cursor");
        Entity camera       = new Entity("camera");
        Entity parentEntity = new Entity("parentEntity");

        scene.addEntity(cursor);
        scene.addEntity(camera);
        scene.addEntity(parentEntity);

        int height = EngineConfig.instance.windowHeight;
        int width = EngineConfig.instance.windowWidth;

        int wCount      = 3;
        int hCount      = 3;

        int xOffsetLeft = 48;
        int zOffsetUp   = 48;

        int heightStep  = height / hCount;
        int widthStep   = width  / wCount;
        for (int h = 0; h < height; h += heightStep) {
            for (int w = 0; w < width; w += widthStep) {
                Entity generatedButton = new Entity("g_button_" + h + "_" + w);
                scene.addEntity(generatedButton);

                Transform    transform = new Transform();
                MeshCollider collider  = new MeshCollider();
                Button       button    = new Button();
                MeshRenderer renderer  = new MeshRenderer();

                componentManager.addComponent(generatedButton, transform);
                componentManager.addComponent(generatedButton, collider);
                componentManager.addComponent(generatedButton, button);
                componentManager.addComponent(generatedButton, renderer);

                transform.entity = generatedButton;
                collider.entity  = generatedButton;
                renderer.entity  = generatedButton;
                generatedButton.transform = transform;
                generatedButton.transform.moveTo(w + (float) widthStep / 2, 0f, h + (float) heightStep / 2);

                renderer.shader = Shader.GUI;

//                collider.body.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
//                collider.body.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);

                button.buttonShape.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                button.buttonShape.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);


                entityManager.linkWithParent(parentEntity, generatedButton);
            }
        }


        Transform transform = new Transform();
        MeshCollider meshCollider = new MeshCollider();
        VisualCursor visualCursor = new VisualCursor();
        MeshRenderer renderer = new MeshRenderer();

        transform.entity = cursor;
        meshCollider.entity = cursor;
        visualCursor.entity = cursor;
        renderer.entity = cursor;

        componentManager.addComponent(cursor, transform);
        componentManager.addComponent(cursor, meshCollider);
        componentManager.addComponent(cursor, visualCursor);
        componentManager.addComponent(cursor, renderer);

    }

    private void initScene1BrokenCursor() {
        Scene scene = new Scene("scene name");

        Entity cursor       = new Entity("cursor");
        Entity camera       = new Entity("camera");
        Entity parentEntity = new Entity("parentEntity");

        scene.addEntity(cursor);
        scene.addEntity(camera);
        scene.addEntity(parentEntity);

        int height = EngineConfig.instance.windowHeight;
        int width = EngineConfig.instance.windowWidth;

        int wCount      = 3;
        int hCount      = 3;

        int xOffsetLeft = 64;
        int zOffsetUp   = 64;

        int heightStep  = height / hCount;
        int widthStep   = width  / wCount;
        for (int h = 0; h < height; h += heightStep) {
            for (int w = 0; w < width; w += widthStep) {
                Entity generatedButton = new Entity("g_button_" + h + "_" + w);
                scene.addEntity(generatedButton);

                Transform    transform = new Transform();
                MeshCollider collider  = new MeshCollider();
                Button       button    = new Button();

                componentManager.addComponent(generatedButton, transform);
                componentManager.addComponent(generatedButton, collider);
                componentManager.addComponent(generatedButton, button);

                transform.entity = generatedButton;
                collider.entity  = generatedButton;
                generatedButton.transform = transform;
                generatedButton.transform.moveTo(w + (float) widthStep / 2, 0f, h + (float) heightStep / 2);

                collider.body.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                collider.body.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);

                button.buttonShape.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                button.buttonShape.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);


                entityManager.linkWithParent(parentEntity, generatedButton);
            }
        }


        Transform transform = new Transform();
        MeshCollider meshCollider = new MeshCollider();
        VisualCursor visualCursor = new VisualCursor();

        transform.entity = cursor;
        meshCollider.entity = cursor;
        visualCursor.entity = cursor;

        componentManager.addComponent(cursor, transform);
        componentManager.addComponent(cursor, meshCollider);
        componentManager.addComponent(cursor, visualCursor);

    }

    private void initScene2() {
        Entity rendererEntity = new Entity("screen");
//        Entity cursor         = new Entity("cursor");

        Transform transform = new Transform();
        Renderer  renderer  = new Renderer();

        renderer.texture    = new Texture("core/assets/textures/screen-frame-1024.png");

        componentManager.addComponent(rendererEntity, transform);
        componentManager.addComponent(rendererEntity, renderer);

//        transform = new Transform();
//        MeshCollider meshCollider = new MeshCollider();
//        renderer  = new Renderer();
//        VisualCursor visualCursor = new VisualCursor();

//        renderer.texture    = new Texture("core/assets/textures/bg.jpeg");

//        componentManager.addComponent(cursor, transform);
//        componentManager.addComponent(cursor, renderer);
//        componentManager.addComponent(cursor, visualCursor);
    }

    private void initScene4() {
        Entity testObject   = new Entity("testObject");
        Entity camera       = new Entity("camera");

        Transform transform = new Transform();
        Transform cameraTransform = new Transform();
        MeshCollider meshCollider = new MeshCollider();
        VisualCursor testObjectComponent = new VisualCursor();
        Camera cameraComponent = new Camera();

        transform.entity = testObject;
        meshCollider.entity = testObject;
        testObjectComponent.entity = testObject;
        cameraTransform.entity = camera;
        cameraComponent.entity = camera;

        componentManager.addComponent(testObject, transform);
        componentManager.addComponent(testObject, meshCollider);
        componentManager.addComponent(testObject, testObjectComponent);
        componentManager.addComponent(camera, cameraTransform);
        componentManager.addComponent(camera, cameraComponent);

//        em.take(em.create("testObject"))
//                .add(Transform.class, MeshCollider.class, )
    }

}
