package ecs;

import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.architecture.TreeEntityManager;
import ecs.config.EngineConfig;
import ecs.graphics.Window;
import ecs.managment.SystemManager;
import ecs.scene.Scene;
import ecs.systems.GameLogic;
import ecs.systems.GameLogicUpdater;
import ecs.utils.Logger;

// todo:
//  - relative position (to parent entities)
//  - create serialization/deserialization flow for scenes (XML for a while)
//  - add stateful animation controller
//  - add gamepad support (also xinput https://github.com/StrikerX3/JXInput)
//  - dependency injection manager, that works on application start in main method
//  - editor mode (Vim like behaviour)
//    - developer console
//    - editor visual cursor, that can select and manipulate objects in scene
public class Engine {
    public static Engine     engine;
    public final  Window     window;
    public final GameLogic gameLoop;

    public EngineConfig config;

    public Engine(Window window) {
        this();
    }

    public Engine() {
        Logger.info("Initializing engine..");

        window = new Window("windowTitle", 512, 512, false);
        gameLoop = new GameLogicUpdater(window);
        engine = this;

        setDataManagers(gameLoop);

        Logger.info("Engine initialization succeeded");
    }

    private void setDataManagers(GameLogic gameLogic) {
        EntityManager entityManager = TreeEntityManager.getInstance();
        SystemManager systemManager = SystemManager.getInstance();
        ComponentManager componentManager = ComponentManager.getInstance();

        componentManager.setDataManagers(entityManager, systemManager);
        ((GameLogicUpdater) gameLoop).setDataManagers(entityManager, componentManager, systemManager);
    }

    public void setScene(Scene scene) {
        this.gameLoop.setScene(scene);
    }

    public void run() {
        if (window == null) {
            Logger.error("Window is not set-up. Cannot run engine");
            return;
        }

        window.init();
        gameLoop.run();
        window.destroy();
    }
}
