package org.north.core.config;

import org.north.core.systems.GameLogic;
import org.north.core.architecture.ComponentManager;
import org.north.core.architecture.EntityManager;
import org.north.core.architecture.TreeEntityManager;
import org.north.core.graphics.Window;
import org.north.core.managment.SystemManager;
import org.north.core.systems.GameLogicUpdater;
import org.north.core.utils.Logger;

public class EngineConfig {
    public static final EntityManager entityManager = TreeEntityManager.getInstance();
    public static final ComponentManager componentManager = ComponentManager.getInstance();
    public static final SystemManager systemManager = SystemManager.getInstance();
    public static final int MAX_ENTITY_QUANTITY = 4096;
    public static final int MAX_COMPONENT_PER_ENTITY_QUANTITY = 128;
    public static final EngineConfig instance = new EngineConfig();

    static {
        Logger.setLogWriter(new Logger.ConsoleLogWriter());
        componentManager.setDataManagers(entityManager, systemManager);
    }

    public final int windowWidth = 512;
    public final int windowHeight = 512;
    public final boolean vsync = false;
    public final String windowTitle = "test_engine";
    public final Window window = new Window(windowTitle, windowWidth, windowHeight, vsync);
    public final GameLogic gameLogic = new GameLogicUpdater(window);

}
