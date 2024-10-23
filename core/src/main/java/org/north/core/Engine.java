package org.north.core;

import org.north.core.architecture.entity.ComponentManager;
import org.north.core.config.EngineConfig;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.management.SystemManager;
import org.north.core.scene.Scene;
import org.north.core.system.Pipeline;

public class Engine {
    public final Window window;
    public final Pipeline pipeline;
    public final ApplicationContext context;

    public Engine() throws Exception {
        // Logger.info("Initializing engine..");

        context = new ApplicationContext();

        context.addDependencies(new Class[]{
                EngineConfig.class, Window.class, SystemManager.class,
                ComponentManager.class,  Graphics.class, Pipeline.class
        });

        window = context.getDependency(Window.class);
        pipeline = context.getDependency(Pipeline.class);

        // Logger.info("Engine initialization succeeded");
    }

    public void setScene(Scene scene) {
        this.pipeline.setScene(scene);
    }

    public void run() {
        Graphics graphics = context.getDependency(Graphics.class);
        window.init(graphics);
        pipeline.run();
        window.destroy(graphics);
    }

}
