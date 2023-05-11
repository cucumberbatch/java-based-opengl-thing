package ecs;

import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.architecture.TreeEntityManager;
import ecs.config.EngineConfig;
import ecs.gl.Window;
import ecs.managment.SystemManager;
import ecs.systems.GameLogicUpdater;
import ecs.utils.Logger;

//todo:
// * relative position (to parent entities)
// * create serialization/deserialization flow for scenes
// * add stateful animation controller
// * add gamepad support (also xinput https://github.com/StrikerX3/JXInput)
public class Engine extends Thread {
    public static Engine     engine;
    public final  Window     window;
    public final  GameLogic  gameLoop;

    public EngineConfig config;

    public Engine() {
        this(new EngineConfig());
    }

    public Engine(EngineConfig engineConfig) {
        this(engineConfig.window, engineConfig.gameLogic);
        this.config = engineConfig;
    }

    public Engine(Window window, GameLogic logic) {
        Logger.info("Initializing engine..");

        this.window = window;
        gameLoop = logic;
        setDataManagers(gameLoop);
        engine = this;
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

    @Override
    public void run() {
        if (window == null) return;
        window.init();
        gameLoop.run();
        window.destroy();
    }
}
