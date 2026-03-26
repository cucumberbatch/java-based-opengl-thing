package org.north.core.scene;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.north.core.architecture.entity.ComponentContainer;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.architecture.entity.Entity;
import org.north.core.architecture.tree.v2.TreeNode;
import org.north.core.component.*;
import org.north.core.graphics.PredefinedMeshes;
import org.north.core.graphics.Texture;
import org.north.core.graphics.shader.SimpleColorShader;
import org.north.core.graphics.shader.TextureShader;

import java.util.List;

public class DefaultSceneComposer implements SceneComposer {
    @Override
    public void compose(final TreeNode<Entity> sceneRoot, final ComponentManager cm) {
        // initCubeAndCamera(sceneRoot, cm);
        // initReferenceScene(sceneRoot, cm);
        // testScene(sceneRoot, cm);
        // initSpaceshipOnScreen(sceneRoot, cm);
        testManyTransparentCubesGrid(sceneRoot, cm);
    }

    private void testManyTransparentCubesGrid(TreeNode<Entity> root, ComponentManager cm) {
        GL30.glClearColor(.1f, .1f, .1f, 1f);

        Entity camera = new Entity("camera");
        cm.add(camera, Transform.class, Camera.class, CameraControls.class);
        root.add(camera);

        int N = 2;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    Entity cube = new Entity("cube_" + i + "_" + j + "_" + k);
                    final float x = i;
                    final float z = j;
                    final float y = k;

                    Transform transform = new Transform();
                    transform.moveTo(0.5f * x, 0.5f * y, 0.5f * z);
                    transform.rescaleTo(0.4999f, 0.4999f, 0.4999f);
                    
                    MeshRenderer meshRenderer = new MeshRenderer();
                    meshRenderer.shader = new SimpleColorShader();
                    meshRenderer.mesh   = PredefinedMeshes.CUBE;
                    meshRenderer.color.set(0, 0, 0, 0f);

                    cm.add(cube, transform);
                    cm.add(cube, meshRenderer);
                    cm.add(cube, CubeColorSwitcher.class).acc = (x / N) * (y / N) * (z / N);

                    root.add(cube);
//                    root = cube;
                }
            }
        }
    }

    private void testScene(TreeNode<Entity> root, ComponentManager cm) {
        Entity testCube = new Entity("test_cube");

        Transform transform = new Transform();
        transform.moveTo(0.5f, 0.5f, 0.5f);
        transform.rescaleTo(0.8f, 0.8f, 0.8f);

        MeshRenderer meshRenderer = new MeshRenderer();
        meshRenderer.shader = new SimpleColorShader();
        // meshRenderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
        meshRenderer.mesh   = PredefinedMeshes.CUBE;
        meshRenderer.color.set(0.42f, 0.42f, 0.54f, 0.5f);
        
        cm.add(testCube, transform);
        cm.add(testCube, meshRenderer);

        Entity camera = new Entity("camera");

        cm.add(camera, Transform.class, Camera.class, CameraControls.class);

//        transform = (Transform) components.get(0);
//        transform.getPosition().set(0.5f, 0.5f, 0.5f);
//        transform.scale.set(0.8f, 0.8f, 0.8f);

        root.add(testCube);
        root.add(camera);

    }

    private void initCubeAndCamera(TreeNode<Entity> root, ComponentManager cm) {
        Entity camera = new Entity("camera");
        Entity center = new Entity("center");
        Entity left   = new Entity("left");
        Entity right  = new Entity("right");

        root.add(camera);
        root.add(center);
        root.add(left);
        root.add(right);

        List<? extends Component> components;
        Transform    transform;
        MeshRenderer renderer;

        components = cm.add(camera, Transform.class, Camera.class, CameraControls.class);

        ((Transform) components.get(0)).moveTo(0, 0, 1);


        components = cm.add(center, Transform.class, MeshRenderer.class);

        renderer = (MeshRenderer) components.get(1);
        renderer.mesh       = PredefinedMeshes.QUAD;
        renderer.color      = new Vector4f(1, 1, 1, 1);
        renderer.shader     = new TextureShader();
        renderer.texture    = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
        //        renderer.texture2 = new Texture("core/src/main/resources/assets/textures/Capture001.png");
        renderer.renderType = GL11.GL_TRIANGLES;


        components = cm.add(left, Transform.class, MeshRenderer.class);

        transform = (Transform) components.get(0);
        transform.moveTo(-2f, 0f, 0f);
        //        transform.scale = new Vector3f(0.5f, 0.2f, 0.2f);

        renderer = (MeshRenderer) components.get(1);
        renderer.mesh       = PredefinedMeshes.QUAD;
        renderer.color      = new Vector4f(1, 1, 1, 1);
        renderer.shader     = new TextureShader();
        //        renderer.texture2 = new Texture("core/src/main/resources/assets/textures/spaceship-16.png");
        renderer.texture    = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
        //        renderer.texture = new Texture("core/src/main/resources/assets/textures/Capture001.png");
        renderer.renderType = GL11.GL_TRIANGLES;


        components = cm.add(right, Transform.class, MeshRenderer.class);

        transform = (Transform) components.get(0);
        transform.moveTo(2f, 0f, 0f);
        //        transform.rotation = new Vector3f(0f, -5f, 0f);
        //        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = (MeshRenderer) components.get(1);
        renderer.mesh       = PredefinedMeshes.CUBE;
        renderer.shader     = new SimpleColorShader();
        renderer.color      = new Vector4f(1, 0, 0, 1);
        //        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
        renderer.renderType = GL11.GL_TRIANGLES;

    }

    private void initReferenceScene(TreeNode<Entity> root, ComponentManager cm) {
        Entity referenceBox = new Entity("referenceBox");

        List<? extends Component> componentList =
                cm.add(referenceBox, Transform.class, MeshRenderer.class, RigidBody.class);

        MeshRenderer renderer = (MeshRenderer) componentList.get(1);
        renderer.shader = new SimpleColorShader();
        renderer.color  = new Vector4f(0.25f, 0.5f, 0.8f, 1f);
        renderer.mesh   = PredefinedMeshes.QUAD;

        root.add(referenceBox);

        if (root.findFirst(entity -> cm.has(entity, Camera.class)) == null) {
            Entity camera = new Entity("camera");

            cm.add(camera, Transform.class, Camera.class, CameraControls.class, PlayerControls.class);

            referenceBox.add(camera);
        }
    }

    private void initSpaceshipOnScreen(TreeNode<Entity> root, ComponentManager cm) {
        Transform    transform;
        MeshRenderer renderer;

        // TV Screen object
        Entity tvScreen = new Entity("tvScreen");
        transform = cm.add(tvScreen, Transform.class);
        transform.rescaleTo(new Vector3f(2, 2, 2));

        renderer = cm.add(tvScreen, MeshRenderer.class);
        renderer.shader  = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");

        // Player spaceship in the middle of the screen
        Entity player = new Entity("player");
        transform = cm.add(player, Transform.class);
        transform.moveTo(new Vector3f(0f, 0f, 1f));
        transform.rescaleTo(0.2f, 0.2f, 0.2f);

        renderer = cm.add(player, MeshRenderer.class);
        renderer.shader  = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/spaceship-16.png");

        cm.add(player, CloudEmitter.class);


        Entity gasCloudSpawner = new Entity("gasCloudSpawner");
        transform = cm.add(gasCloudSpawner, Transform.class);
        transform.moveTo( new Vector3f(0f, -0.225f, 0.5f));


        /*

        // Spaceship gas clouds
        Entity cloud = em.create("cloud");
        transform = cm.take(cloud).add(Transform.class);
        transform.moveTo(new Vector3f(0f, -0.25f, 1f);
        transform.scale = new Vector3f(0.2f, 0.2f, 0.2f);

        renderer = cm.take(cloud).add(MeshRenderer.class);
        renderer.shader = new AtlasTextureAnimationShader(6, 12, 12);
        renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

        GasCloud animation = cm.take(cloud).add(GasCloud.class);

         */

        // Background texture behind tv screen, player and other gameplay objects
        Entity background = new Entity("background");
        transform = cm.add(background, Transform.class);
        transform.moveTo(new Vector3f(0f, 0f, 2f));
        transform.rescaleTo(new Vector3f(2, 2, 2));

        renderer = cm.add(background, MeshRenderer.class);
        renderer.shader  = new TextureShader();
        renderer.texture = new Texture("core/src/main/resources/assets/textures/screen-background-1024.png");

        // Camera
        Entity camera = new Entity("camera");
        transform = cm.add(camera, Transform.class);
        transform.moveTo( new Vector3f(0f, 0f, 0f));

        cm.add(camera, Camera.class);

        // World that moves when player is "moving"
        Entity movableWorld = new Entity("movableWorld");
        cm.add(movableWorld, RigidBody.class);
        transform = cm.add(movableWorld, Transform.class);
        //        transform.scale = new Vector3f(5, 5, 5);

        root.add(camera);
        root.add(movableWorld);

        camera.add(tvScreen);

        tvScreen.add(background);
        tvScreen.add(player);

        player.add(gasCloudSpawner);
    }


}
