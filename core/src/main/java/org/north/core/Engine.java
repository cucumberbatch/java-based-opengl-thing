package org.north.core;

import org.north.core.architecture.TreeEntityManager;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.config.EngineConfig;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.managment.SystemManager;
import org.north.core.scene.Scene;
import org.north.core.systems.GameLogicUpdater;

public class Engine {
    public final EngineConfig config;
    public final Window window;
    public final GameLogicUpdater gameLoop;
    public final ApplicationContext context;

    public Engine(EngineConfig engineConfig) throws Exception {
        // Logger.info("Initializing engine..");

        config = engineConfig;
        context = new ApplicationContext(engineConfig);

        context.addDependencies(new Class[]{
                Window.class, TreeEntityManager.class, SystemManager.class,
                ComponentManager.class,  Graphics.class, GameLogicUpdater.class
        });

        window = context.getDependency(Window.class);
        gameLoop = context.getDependency(GameLogicUpdater.class);

        // Logger.info("Engine initialization succeeded");
    }

    public void setScene(Scene scene) {
        this.gameLoop.setScene(scene);
    }

    public void run() {
        window.init();
        gameLoop.run();
        window.destroy();
    }
}
