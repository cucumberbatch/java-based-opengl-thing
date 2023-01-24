package ecs.systems;

import ecs.Engine;
import ecs.Scene;
import ecs.components.InitEntities;
import ecs.components.Transform;
import ecs.entities.Entity;
import vectors.Vector3f;

import static ecs.utils.ApplicationConfig.DEVELOP;

public class InitEntitiesSystem extends AbstractECSSystem<InitEntities> {
    
    @Override
    public int getWorkflowMask() {
        return INIT_MASK;
    }

    @Override
    public void init() throws Exception {
        /*
        super.init();

        Engine engine = Engine.getInstance();
        Scene scene = engine.getScene();

        // Entity centerPlane = new Entity("centered_plane", engine, scene);
        Entity cursor      = new Entity("cursor", engine, scene);
//        Entity button      = new Entity("side_plane", engine, scene);
        Entity camera      = new Entity("camera", engine, scene);
        Entity parentEntity = new Entity("parentEntity", engine, scene);

        int height = engine.window().getHeight();
        int width = engine.window().getWidth();

        int wCount      = 3;
        int hCount      = 3;

        int xOffsetLeft = 48;
        int zOffsetUp   = 24;

        int heightStep  = height / hCount;
        int widthStep   = width  / wCount;
        for (int h = 0; h < height; h += heightStep) {
            for (int w = 0; w < width; w += widthStep) {
                Entity generatedButton = new Entity("g_button_" + h + "_" + w, engine, scene);
                if (!DEVELOP) {
                    generatedButton.addComponent(Type.TRANSFORM);
                    generatedButton.addComponent(ECSSystem.Type.MESH_COLLIDER);
                    Transform transform = generatedButton.transform;
                    generatedButton.transform.position.set(w + widthStep / 2, 0f, h + heightStep / 2);
                    MeshCollider meshCollider = generatedButton.getComponent(ECSSystem.Type.MESH_COLLIDER);
                    meshCollider.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                    meshCollider.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);
                    generatedButton.attachTo(parentEntity);
                    engine.addEntity(generatedButton);
                }
            }
        }


        engine.addEntity(cursor);
        engine.addEntity(camera);

        cursor.transform.position = new Vector3f(0f, 0f, 0f);
        camera.transform.position = new Vector3f(0f, 0f, 0f);

        if (!DEVELOP) {
            cursor.addComponent(ECSSystem.Type.MESH_COLLIDER);
            cursor.addComponent(ECSSystem.Type.PLANE);
            camera.addComponent(ECSSystem.Type.CAMERA);
        }

         */

    }
}
