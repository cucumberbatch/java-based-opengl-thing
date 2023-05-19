package ecs.systems;

import ecs.Engine;
import ecs.Scene;
import ecs.components.*;
import ecs.config.EngineConfig;
import ecs.entities.Entity;

public class InitEntitiesSystem extends AbstractSystem<InitEntities> {

    public InitEntitiesSystem() {
        super();
    }
    
    @Override
    public int getWorkflowMask() {
        return INIT_MASK;
    }

    @Override
    public void init() throws RuntimeException {

        super.init();

        initScene3();

    }

    private void initScene1() {
        Engine engine = Engine.engine;
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

                componentManager.addComponent(generatedButton, transform);
                componentManager.addComponent(generatedButton, collider);
                componentManager.addComponent(generatedButton, button);

                transform.entity = generatedButton;
                collider.entity  = generatedButton;
                generatedButton.transform = transform;
                generatedButton.transform.position.set(w + (float) widthStep / 2, 0f, h + (float) heightStep / 2);

                collider.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                collider.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);

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
        Engine engine   = Engine.engine;
        Scene  scene    = new Scene("scene name 2");

        Entity rendererEntity = new Entity("renderer");

        scene.addEntity(rendererEntity);

        Transform transform = new Transform();
        Renderer renderer = new Renderer();

        componentManager.addComponent(rendererEntity, transform);
        componentManager.addComponent(rendererEntity, renderer);
    }

    private void initScene3() {
        Engine engine = Engine.engine;
        Scene scene = new Scene("scene name 3");

        Entity cursor       = new Entity("cursor");
        Entity camera       = new Entity("camera");
        Entity parentEntity = new Entity("parentEntity");

        scene.addEntity(cursor);
        scene.addEntity(camera);
        scene.addEntity(parentEntity);

        int height = EngineConfig.instance.windowHeight;
        int width = EngineConfig.instance.windowWidth;

        int wCount      = 8;
        int hCount      = 8;

        int xOffsetLeft = 30;
        int zOffsetUp   = 30;

        int heightStep  = height / hCount;
        int widthStep   = width  / wCount;
        for (int h = 0; h < height + heightStep; h += heightStep) {
            for (int w = 0; w < width + widthStep; w += widthStep) {
                Entity generatedButton = new Entity("g_button_" + h + "_" + w);
                scene.addEntity(generatedButton);

                Transform    transform = new Transform();
                MeshCollider collider  = new MeshCollider();
                Button       button    = new Button();

                collider.isStatic = true;

                componentManager.addComponent(generatedButton, transform);
                componentManager.addComponent(generatedButton, collider);
                componentManager.addComponent(generatedButton, button);

                transform.entity = generatedButton;
                collider.entity  = generatedButton;
                generatedButton.transform = transform;
                generatedButton.transform.position.set(w + (float) widthStep / 2, 0f, h + (float) heightStep / 2);

                collider.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                collider.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);

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

}
