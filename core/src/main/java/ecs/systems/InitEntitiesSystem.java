package ecs.systems;

import ecs.Engine;
import ecs.Scene;
import ecs.components.Button;
import ecs.components.InitEntities;
import ecs.components.VisualCursor;
import ecs.components.Transform;
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

        Engine engine = Engine.engine;
        Scene scene = new Scene("scene name");

        // Entity centerPlane = new Entity("centered_plane", engine, scene);
        Entity cursor      = new Entity("cursor");
//        Entity button      = new Entity("side_plane", engine, scene);
        Entity camera      = new Entity("camera");
        Entity parentEntity = new Entity("parentEntity");

        scene.addEntity(cursor);
        scene.addEntity(camera);
        scene.addEntity(parentEntity);

        int height = EngineConfig.instance.windowHeight;
        int width = EngineConfig.instance.windowWidth;

        int wCount      = 3;
        int hCount      = 3;

        int xOffsetLeft = 48;
        int zOffsetUp   = 24;

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
//                    engine.addEntity(generatedButton);
            }
        }


//        engine.addEntity(cursor);
//        engine.addEntity(camera);
//
//        cursor.transform.position = new Vector3f(0f, 0f, 0f);
//        camera.transform.position = new Vector3f(0f, 0f, 0f);
//
        Transform transform = new Transform();
        MeshCollider meshCollider = new MeshCollider();
        VisualCursor visualCursor = new VisualCursor();

        transform.entity = cursor;
        meshCollider.entity = cursor;
        visualCursor.entity = cursor;

        componentManager.addComponent(cursor, transform);
        componentManager.addComponent(cursor, meshCollider);
        componentManager.addComponent(cursor, visualCursor);

//            camera.addComponent(ECSSystem.Type.CAMERA);

    }
}
