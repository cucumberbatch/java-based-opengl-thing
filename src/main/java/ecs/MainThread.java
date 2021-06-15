package ecs;

import ecs.entities.Entity;
import ecs.math.Vector3f;
import ecs.systems.ECSSystem;

public class MainThread {
    public static void main(String[] args) {
        int width = 640;
        int height = 480;

        Engine engine = new Engine("test_engine", width, height, true, new GameLogic());

        Scene scene = new Scene();

        Entity e        = new Entity("entity", engine, scene);
        Entity camera   = new Entity("camera", engine, scene);

        engine.addEntity(e);
        engine.addEntity(camera);


        camera.transform.position = new Vector3f(0f, 0f, 0f);
//        e.transform.position.set(-2f, 0.4f, 0.3f);

        e.addComponent(ECSSystem.Type.RENDERER);

        camera.addComponent(ECSSystem.Type.CAMERA);

//        JFrame frame = new JFrame("JFrame Example");
//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout());
//        JButton button = new JButton();
//        button.setText("Button");
//        panel.add(button);
//        frame.add(panel);
//        frame.setSize(200, 300);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);


        engine.run();

    }

}