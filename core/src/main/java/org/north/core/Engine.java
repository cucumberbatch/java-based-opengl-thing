package org.north.core;

import org.north.core.architecture.entity.ComponentManager;
import org.north.core.config.EngineConfig;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.managment.SystemManager;
import org.north.core.scene.Scene;
import org.north.core.system.Pipeline;
import org.north.core.utils.logger.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Engine {
    public final Window window;
    public final Pipeline pipeline;
    public final ApplicationContext context;
    private EditorFrame frame;

    public Engine() throws Exception {
        // Logger.info("Initializing engine..");

        context = new ApplicationContext();

        context.addDependencies(new Class[]{
                EngineConfig.class, Window.class, SystemManager.class,
                ComponentManager.class,  Graphics.class, Pipeline.class
        });

        window = context.getDependency(Window.class);
        pipeline = context.getDependency(Pipeline.class);


        SwingUtilities.invokeLater(() -> {
            //        JFrame.setDefaultLookAndFeelDecorated(true);
            frame = new EditorFrame("Test frame");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            JLabel label = new JLabel("Test label");
            frame.getContentPane().add(label);

            frame.setPreferredSize(new Dimension(200, 100));
            frame.setLocation(new Point(200, 400));

            frame.pack();
            frame.setVisible(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    pipeline.stop();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    super.windowClosing(e);
                }
            });
        });

        // Logger.info("Engine initialization succeeded");
    }

    public void setScene(Scene scene) {
        this.pipeline.setScene(scene);
    }

    public void run() {
        window.init();
        pipeline.run();
        window.destroy();
    }

    static class EditorFrame extends JFrame {
        EditorFrame(String title) {
            super(title);
        }
    }

    static class ComponentPanel extends Panel {

    }


}
