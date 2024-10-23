package org.north.core.config;

public class ApplicationConfig {

    private static final String APPLICATION_VERSION = ApplicationProperties.getProperty("application.version");

    public static final String LOGGER_TIME_INFO_PATTERN = "HH:mm:ss.SSS";

    // Assets
    public static final String ASSETS_DIRECTORY_PATH = "assets\\";

    public static final boolean DEVELOP = true;


    private ApplicationConfig() {}

}
