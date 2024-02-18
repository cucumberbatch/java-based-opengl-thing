package org.north.core.config;

import org.north.core.utils.Logger;

public class EngineConfig {
    public static final int MAX_ENTITY_QUANTITY = 4096;
    public static final int MAX_COMPONENT_PER_ENTITY_QUANTITY = 128;
    public static final EngineConfig instance = new EngineConfig();

    static {
        Logger.setLogWriter(new Logger.ConsoleLogWriter());
    }

    public final int windowWidth = 512;
    public final int windowHeight = 512;
    public final boolean vsync = false;
    public final String windowTitle = "test_engine";

}
