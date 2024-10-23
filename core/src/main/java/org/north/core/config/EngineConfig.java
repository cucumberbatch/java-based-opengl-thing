package org.north.core.config;

public class EngineConfig {
    public static final int MAX_ENTITY_QUANTITY = 4096;
    public static final int MAX_COMPONENT_PER_ENTITY_QUANTITY = 128;
    public static final EngineConfig instance = new EngineConfig();

    public final int windowWidth = 512;
    public final int windowHeight = 512;
    public final boolean vsync = false;
    public final String windowTitle = "Scene view";

}
