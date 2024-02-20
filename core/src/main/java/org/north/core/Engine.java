package org.north.core;

import org.north.core.architecture.tree.EntityTree;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.config.EngineConfig;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.managment.SystemManager;
import org.north.core.scene.Scene;
import org.north.core.system.Pipeline;

public class Engine {
    public final EngineConfig config;
    public final Window window;
    public final Pipeline pipeline;
    public final ApplicationContext context;

    public Engine(EngineConfig engineConfig) throws Exception {
        // Logger.info("Initializing engine..");

        config = engineConfig;
        context = new ApplicationContext(engineConfig);

        context.addDependencies(new Class[]{
                Window.class, EntityTree.class, SystemManager.class,
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
        window.init();
        pipeline.run();
        window.destroy();
    }
}
