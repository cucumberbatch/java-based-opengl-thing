package ecs.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ApplicationConfig {

    private ApplicationConfig() {}

    // Logger
    public static final Logger.Level LOGGER_SEVERITY = Logger.Level.TRACE;
    public static final String LOGGER_TIME_INFO_PATTERN = "HH:mm:ss.SSS";
    public static final String LOG_FILE_PATH = String.format(
            "C:\\Users\\Владислав\\Desktop\\java\\%s.log",
            new SimpleDateFormat("yyyyMMdd'_'HHmmss").format(Date.from(Instant.now())));

    // Assets
    public static final String ASSETS_DIRECTORY_PATH = "assets\\";


}
