package org.north.core;

import org.north.core.architecture.entity.ComponentManager;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.management.SystemManager;
import org.north.core.scene.Scene;
import org.north.core.system.Pipeline;

/**
 * An entry point of all engine dependencies
 */
public class Engine {
    public final Window window;
    public final Graphics graphics;
    public final Pipeline pipeline;
    public final ApplicationContext context;
    public final SystemManager systemManager;
    public final ComponentManager componentManager;

    public Engine() throws Exception {
        context = new ApplicationContext();

        window = new Window();
        context.addDependency(Window.class, window);

        graphics = new Graphics(window);
        context.addDependency(Graphics.class, graphics);

        systemManager = new SystemManager(context);
        context.addDependency(SystemManager.class, systemManager);

        componentManager = new ComponentManager(context);
        context.addDependency(ComponentManager.class, componentManager);

        pipeline = new Pipeline(context);
        context.addDependency(Pipeline.class, pipeline);

    }

    public void run() {
        window.init(graphics);
        pipeline.run();
        window.destroy(graphics);
    }

}
