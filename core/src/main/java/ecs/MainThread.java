package ecs;

import ecs.entities.Entity;
import vectors.Vector3f;
import ecs.systems.ECSSystem;

public class MainThread {
    public static void main(String[] args) {
        int width = 920;
        int height = 570;

        Engine engine = new Engine("test_engine", width, height, true, new GameLogic());

        Scene scene = new Scene();

        // Entity centerPlane = new Entity("centered_plane", engine, scene);
        Entity sidePlane   = new Entity("side_plane", engine, scene);
        Entity camera      = new Entity("camera", engine, scene);

        // engine.addEntity(centerPlane);
        engine.addEntity(sidePlane);
        engine.addEntity(camera);


        camera.transform.position      = new Vector3f(0f, 0f, 0f);
        // centerPlane.transform.position = new Vector3f(0f, 0f, 0f);
        sidePlane.transform.position   = new Vector3f(1f, 0f, -1f);

//        e.transform.position.set(-2f, 0.4f, 0.3f);

        // centerPlane.addComponent(ECSSystem.Type.PLANE);
        sidePlane.addComponent(ECSSystem.Type.PLANE);
        camera.addComponent(ECSSystem.Type.CAMERA);

        engine.run();

    }

}