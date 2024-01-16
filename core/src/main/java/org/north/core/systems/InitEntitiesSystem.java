package org.north.core.systems;

import org.north.core.graphics.*;
import org.north.core.scene.Scene;
import org.north.core.architecture.ComponentManager;
import org.north.core.architecture.entity.ImprovedEntityManager;
import org.north.core.config.EngineConfig;
import org.north.core.entities.Entity;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.InitProcess;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.north.core.components.*;

import java.util.List;

@ComponentHandler(InitEntities.class)
public class InitEntitiesSystem extends AbstractSystem<InitEntities>
        implements InitProcess {

    private final ImprovedEntityManager em = new ImprovedEntityManager(ComponentManager.getInstance());

    @Override
    public void init() throws RuntimeException {
//        initCubeAndCamera();
//        initSpaceshipOnScreen();
        initSprites();
//        initScene4();
//        initSingleButtonScene();
    }

    private void initSpaceshipOnScreen() {
        Transform transform;
        MeshRenderer renderer;

        // TV Screen object
        Entity tvScreen = em.create("tvScreen");
        transform = em.take(tvScreen).add(Transform.class);
        renderer = em.take(tvScreen).add(MeshRenderer.class);

        transform.position = new Vector3f(0, 0, 0f);
        transform.scale = new Vector3f(2, 2, 2);
        transform.rotation = new Vector3f(1, 1, 1);

        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");

        // Player spaceship in the middle of the screen
        Entity player = em.create("player");
        transform = em.take(player).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 1f);
        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = em.take(player).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/spaceship-16.png");

        // Background texture behind tv screen, player and other gameplay objects
        Entity background = em.create("background");
        transform = em.take(background).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 2f);
        transform.scale = new Vector3f(2, 2, 2);

        renderer = em.take(background).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-background-1024.png");

        // Camera
        Entity camera = em.create("camera");
        transform = em.take(camera).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, -1f);

        em.take(camera).add(Camera.class);
    }

    private void initSprites() {
        Transform transform;
        MeshRenderer renderer;

        // TV Screen object
        Entity tvScreen = em.create("tvScreen");
        transform = em.take(tvScreen).add(Transform.class);
        renderer = em.take(tvScreen).add(MeshRenderer.class);

        transform.position = new Vector3f(0, 0, 0f);
        transform.scale = new Vector3f(2, 2, 2);
        transform.rotation = new Vector3f(1, 1, 1);

        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");

        // Player spaceship in the middle of the screen
        Entity player = em.create("player");
        transform = em.take(player).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 1f);
        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = em.take(player).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/spaceship-16.png");

        em.take(player).add(CloudEmitter.class);


        /*

        // Spaceship gas clouds
        Entity cloud = em.create("cloud");
        transform = em.take(cloud).add(Transform.class);
        transform.position = new Vector3f(0f, -0.25f, 1f);
        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = em.take(cloud).add(MeshRenderer.class);
        renderer.shader = new AtlasTextureAnimationShader(6, 12, 12);
        renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

        GasCloud animation = em.take(cloud).add(GasCloud.class);

         */

        // Background texture behind tv screen, player and other gameplay objects
        Entity background = em.create("background");
        transform = em.take(background).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 2f);
        transform.scale = new Vector3f(2, 2, 2);

        renderer = em.take(background).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-background-1024.png");

        // Camera
        Entity camera = em.create("camera");
        transform = em.take(camera).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, -1f);

        em.take(camera).add(Camera.class);
    }

    private void initCubeAndCamera() {
        List<Component> components;
        Transform transform;
        MeshRenderer renderer;

        components = em.take(em.create("camera"))
                .add(Transform.class, Camera.class, CameraControls.class);

        components.get(0).getTransform().moveTo(0, 0, 1);


        components = em.take(em.create("center")).add(Transform.class, MeshRenderer.class);

        renderer = (MeshRenderer) components.get(1);
        renderer.mesh = PredefinedMeshes.QUAD;
        renderer.color = new Vector4f(1, 1, 1, 1);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
//        renderer.texture2 = new Texture("core/src/main/resources/assets/textures/Capture001.png");
        renderer.renderType = GL11.GL_TRIANGLES;


        components = em.take(em.create("left")).add(Transform.class, MeshRenderer.class);

        transform = (Transform) components.get(0);
        transform.moveTo(-2f, 0f, 0f);
//        transform.scale = new Vector3f(0.5f, 0.2f, 0.2f);

        renderer = (MeshRenderer) components.get(1);
        renderer.mesh = PredefinedMeshes.QUAD;
        renderer.color = new Vector4f(1, 1, 1, 1);
        renderer.shader = new TextureShader();
//        renderer.texture2 = new Texture("core/src/main/resources/assets/textures/spaceship-16.png");
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
//        renderer.texture = new Texture("core/src/main/resources/assets/textures/Capture001.png");
        renderer.renderType = GL11.GL_TRIANGLES;


        components = em.take(em.create("right")).add(Transform.class, MeshRenderer.class);

        transform = (Transform) components.get(0);
        transform.moveTo(2f, 0f, 0f);
//        transform.rotation = new Vector3f(0f, -5f, 0f);
//        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = (MeshRenderer) components.get(1);
        renderer.mesh = PredefinedMeshes.CUBE;
        renderer.shader = new SimpleColorShader();
        renderer.color = new Vector4f(1, 0, 0, 1);
//        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
        renderer.renderType = GL11.GL_TRIANGLES;

    }

    private void initSingleButtonScene() {
        Entity eCamera = em.create("camera");
        Entity eButton = em.create("button");

        List<Component> components;
        Transform transform;
        Button button;
        MeshRenderer renderer;
        Camera camera;

        components = em.take(eButton)
                .add(Transform.class, Button.class, MeshRenderer.class);

        transform = ((Transform) components.get(0));
        transform.moveTo(0, 0, 1);

        renderer = ((MeshRenderer) components.get(2));
        renderer.mesh = new Mesh();
        renderer.shader = new SimpleColorShader();
        renderer.color = new Vector4f(0.4f, 0.7f, 0.3f, 1f);
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");

        transform = em.take(eCamera).add(Transform.class);
        camera = em.take(eCamera).add(Camera.class);

        transform.moveTo(0, 0, 0);
        camera.projectionMatrix = CameraSystem.PERSPECTIVE_MATRIX;
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

//                renderer.shader = OldShader.SIMPLE_COLOR_SHADER;

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