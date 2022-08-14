package ecs;

import ecs.components.Button;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.systems.MeshCollider;
import ecs.utils.Logger;
import ecs.utils.Logger.ConsoleLogWriter;
import vectors.Vector3f;
import ecs.systems.ECSSystem;

public class MainThread {
    public static void main(String[] args) {
        Logger.setLogWriter(new ConsoleLogWriter());

        int width = 920;
        int height = 570;

        Engine engine = new Engine("test_engine", width, height, true, new GameLogic());

        Scene scene = new Scene();

        // Entity centerPlane = new Entity("centered_plane", engine, scene);
        Entity cursor      = new Entity("cursor", engine, scene);
//        Entity button      = new Entity("side_plane", engine, scene);
        Entity camera      = new Entity("camera", engine, scene);


        int wCount      = 3;
        int hCount      = 3;

        int xOffsetLeft = 48;
        int zOffsetUp   = 24;

        int heightStep  = height / hCount;
        int widthStep   = width  / wCount;
        for (int h = 0; h < height; h += heightStep) {
            for (int w = 0; w < width; w += widthStep) {
                Entity generatedButton = new Entity("g_button_" + h + "_" + w, engine, scene);
                Transform transform = generatedButton.transform;
                generatedButton.transform.position.set(w + widthStep / 2, 0f, h + heightStep / 2);
                generatedButton.addComponent(ECSSystem.Type.MESH_COLLIDER);
                MeshCollider meshCollider = generatedButton.getComponent(ECSSystem.Type.MESH_COLLIDER);
                meshCollider.mesh.topLeft.set(transform.position.x - xOffsetLeft, transform.position.z - zOffsetUp);
                meshCollider.mesh.bottomRight.set(transform.position.x + xOffsetLeft, transform.position.z + zOffsetUp);
                engine.addEntity(generatedButton);
            }
        }


        engine.addEntity(cursor);
        engine.addEntity(camera);

        cursor.transform.position = new Vector3f(0f, 0f, 0f);
        camera.transform.position = new Vector3f(0f, 0f, 0f);

        cursor.addComponent(ECSSystem.Type.MESH_COLLIDER);
        cursor.addComponent(ECSSystem.Type.PLANE);
        camera.addComponent(ECSSystem.Type.CAMERA);

        engine.run();

    }

}