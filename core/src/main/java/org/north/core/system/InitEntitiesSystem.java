package org.north.core.system;

import org.north.core.context.ApplicationContext;
import org.north.core.graphics.*;
import org.north.core.graphics.shader.SimpleColorShader;
import org.north.core.graphics.shader.TextureShader;
import org.north.core.reflection.di.Inject;
import org.north.core.scene.Scene;
import org.north.core.config.EngineConfig;
import org.north.core.architecture.entity.Entity;
import org.north.core.reflection.ComponentHandler;
import org.north.core.system.process.InitProcess;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.north.core.component.*;

import java.util.List;

@ComponentHandler(InitEntities.class)
public class InitEntitiesSystem extends AbstractSystem<InitEntities>
        implements InitProcess<InitEntities> {

    @Inject
    public InitEntitiesSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(InitEntities initEntities) throws RuntimeException {
        Entity root = et.create("root");
        Transform rootTransform = cm.take(root).add(Transform.class);

//        initSingleButtonScene();
//        initCubeAndCamera();
//        initReferenceScene();
//        initSpaceshipOnScreen();
        initSprites();
//        initScene1();
//        initScene4();
    }

    private void initReferenceScene() {
        Entity referenceBox = et.create("referenceBox");
        Entity camera = et.create("camera");

        camera.setParent(referenceBox);

        cm.take(camera)
                .add(Transform.class, Camera.class, CameraControls.class, PlayerControls.class);

        List<? extends Component> componentList = cm.take(referenceBox)
                .add(Transform.class, MeshRenderer.class, RigidBody.class);

        MeshRenderer renderer = (MeshRenderer) componentList.get(1);
        renderer.shader = new SimpleColorShader();
        renderer.color = new Vector4f(0.25f, 0.5f, 0.8f, 1f);
        renderer.mesh = PredefinedMeshes.QUAD;
    }

    private void initSpaceshipOnScreen() {
        Transform transform;
        MeshRenderer renderer;

        // TV Screen object
        Entity tvScreen = et.create("tvScreen");
        transform = cm.take(tvScreen).add(Transform.class);
        renderer = cm.take(tvScreen).add(MeshRenderer.class);

        transform.position = new Vector3f(0, 0, 0f);
        transform.scale = new Vector3f(2, 2, 2);

        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");

        // Player spaceship in the middle of the screen
        Entity player = et.create("player");
        transform = cm.take(player).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 1f);
        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = cm.take(player).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/spaceship-16.png");

        // Background texture behind tv screen, player and other gameplay objects
        Entity background = et.create("background");
        transform = cm.take(background).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 2f);
        transform.scale = new Vector3f(2, 2, 2);

        renderer = cm.take(background).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-background-1024.png");

        // Camera
        Entity camera = et.create("camera");
        transform = cm.take(camera).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, -1f);

        cm.take(camera).add(Camera.class);
    }

    private void initSprites() {
        Transform transform;
        MeshRenderer renderer;

        // TV Screen object
        Entity tvScreen = et.create("tvScreen");
        transform = cm.take(tvScreen).add(Transform.class);
        transform.scale = new Vector3f(2, 2, 2);

        renderer = cm.take(tvScreen).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");

        // Player spaceship in the middle of the screen
        Entity player = et.create("player");
        transform = cm.take(player).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 1f);
        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = cm.take(player).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/spaceship-16.png");

        cm.take(player).add(CloudEmitter.class);


        Entity gasCloudSpawner = et.create("gasCloudSpawner");
        transform = cm.take(gasCloudSpawner).add(Transform.class);
        transform.position = new Vector3f(0f, -0.225f, 0.5f);


        /*

        // Spaceship gas clouds
        Entity cloud = em.create("cloud");
        transform = cm.take(cloud).add(Transform.class);
        transform.position = new Vector3f(0f, -0.25f, 1f);
        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = cm.take(cloud).add(MeshRenderer.class);
        renderer.shader = new AtlasTextureAnimationShader(6, 12, 12);
        renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

        GasCloud animation = cm.take(cloud).add(GasCloud.class);

         */

        // Background texture behind tv screen, player and other gameplay objects
        Entity background = et.create("background");
        transform = cm.take(background).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 2f);
        transform.scale = new Vector3f(2, 2, 2);

        renderer = cm.take(background).add(MeshRenderer.class);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-background-1024.png");

        // Camera
        Entity camera = et.create("camera");
        transform = cm.take(camera).add(Transform.class);
        transform.position = new Vector3f(0f, 0f, 0f);

        cm.take(camera).add(Camera.class);

        // World that moves when player is "moving"
        Entity movableWorld = et.create("movableWorld");
        cm.take(movableWorld).add(RigidBody.class);
        transform = cm.take(movableWorld).add(Transform.class);
//        transform.scale = new Vector3f(5, 5, 5);

        tvScreen.setParent(camera);
        background.setParent(tvScreen);
        player.setParent(tvScreen);
        gasCloudSpawner.setParent(player);

    }

    private void initCubeAndCamera() {
        List<? extends Component> components;
        Transform transform;
        MeshRenderer renderer;

        components = cm.take(et.create("camera"))
                .add(Transform.class, Camera.class, CameraControls.class);

        components.get(0).getTransform().moveTo(0, 0, 1);


        components = cm.take(et.create("center")).add(Transform.class, MeshRenderer.class);

        renderer = (MeshRenderer) components.get(1);
        renderer.mesh = PredefinedMeshes.QUAD;
        renderer.color = new Vector4f(1, 1, 1, 1);
        renderer.shader = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
//        renderer.texture2 = new Texture("core/src/main/resources/assets/textures/Capture001.png");
        renderer.renderType = GL11.GL_TRIANGLES;


        components = cm.take(et.create("left")).add(Transform.class, MeshRenderer.class);

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


        components = cm.take(et.create("right")).add(Transform.class, MeshRenderer.class);

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
        Entity eCamera = et.create("camera");
        Entity eButton = et.create("button");

        List<? extends Component> components;
        Transform transform;
        Button button;
        MeshRenderer renderer;
        Camera camera;

        components = cm.take(eButton)
                .add(Transform.class, Button.class, MeshRenderer.class);

        transform = ((Transform) components.get(0));
        transform.moveTo(0, 0, 1);

        renderer = ((MeshRenderer) components.get(2));
        renderer.mesh = new Mesh();
        renderer.shader = new SimpleColorShader();
        renderer.color = new Vector4f(0.4f, 0.7f, 0.3f, 1f);
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");

        transform = cm.take(eCamera).add(Transform.class);
        camera = cm.take(eCamera).add(Camera.class);

        transform.moveTo(0, 0, 0);
        camera.projectionMatrix = CameraSystem.PERSPECTIVE_MATRIX;
    }

    private void initScene1() {
        Entity cursor = et.create("cursor");
        Entity camera = et.create("camera");
        Entity parentEntity = et.create("parentEntity");

        cm.take(camera).add(Transform.class, Camera.class);

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
                Entity generatedButton = et.create("g_button_" + h + "_" + w);

                List<? extends Component> componentList = cm.take(generatedButton)
                        .add(Transform.class, MeshCollider.class, Button.class, MeshRenderer.class);

                Transform transform = (Transform) componentList.get(0);
                MeshCollider collider = (MeshCollider) componentList.get(1);
                Button button = (Button) componentList.get(2);
                MeshRenderer renderer = (MeshRenderer) componentList.get(3);

//                renderer.shader = new SimpleColorShader();

//                collider.body.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
//                collider.body.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);

                transform.moveTo(w + (float) widthStep / 2, 0f, h + (float) heightStep / 2);
                button.buttonShape.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                button.buttonShape.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);


                generatedButton.setParent(parentEntity);
            }
        }

        List<? extends Component> componentList = cm.take(cursor)
                .add(Transform.class, MeshCollider.class, VisualCursor.class, MeshRenderer.class);

        Transform transform = (Transform) componentList.get(0);
        MeshCollider meshCollider = (MeshCollider) componentList.get(1);
        VisualCursor visualCursor = (VisualCursor) componentList.get(2);
        MeshRenderer renderer = (MeshRenderer) componentList.get(3);
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

                List<? extends Component> componentList = cm.take(generatedButton)
                        .add(Transform.class, MeshCollider.class, Button.class);

                Transform transform = (Transform) componentList.get(0);
                MeshCollider collider = (MeshCollider) componentList.get(1);
                Button button = (Button) componentList.get(2);

                transform.entity = generatedButton;
                collider.entity  = generatedButton;
                generatedButton.transform = transform;
                generatedButton.transform.moveTo(w + (float) widthStep / 2, 0f, h + (float) heightStep / 2);

                collider.body.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                collider.body.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);

                button.buttonShape.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                button.buttonShape.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);


                generatedButton.setParent(parentEntity);
            }
        }

        List<? extends Component> componentList = cm.take(cursor)
                .add(Transform.class, MeshCollider.class, VisualCursor.class);

        Transform transform = (Transform) componentList.get(0);
        MeshCollider collider = (MeshCollider) componentList.get(1);
        VisualCursor visualCursor = (VisualCursor) componentList.get(2);

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

        cm.take(testObject).add(Transform.class, MeshCollider.class, VisualCursor.class);
        cm.take(camera).add(Transform.class, Camera.class);


//        cm.take(em.create("testObject"))
//                .add(Transform.class, MeshCollider.class, )
    }

}
