package ecs.config;

import ecs.systems.GameLogic;
import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.architecture.TreeEntityManager;
import ecs.graphics.Window;
import ecs.managment.SystemManager;
import ecs.systems.GameLogicUpdater;
import ecs.utils.Logger;

public class EngineConfig {
    public final int windowWidth    = 512;
    public final int windowHeight   = 512;
    public final boolean vsync      = false;
    public final String windowTitle = "test_engine";

    public final Window        window        = new Window(windowTitle, windowWidth, windowHeight, vsync);
    public final GameLogic     gameLogic     = new GameLogicUpdater(window);

    public static final EntityManager    entityManager    = TreeEntityManager.getInstance();
    public static final ComponentManager componentManager = ComponentManager.getInstance();
    public static final SystemManager    systemManager    = SystemManager.getInstance();

    public static final int MAX_ENTITY_QUANTITY = 4096;
    public static final int MAX_COMPONENT_PER_ENTITY_QUANTITY = 128;

    public static final EngineConfig instance = new EngineConfig();

    static {
        Logger.setLogWriter(new Logger.ConsoleLogWriter());
        componentManager.setDataManagers(entityManager, systemManager);
    }

}
