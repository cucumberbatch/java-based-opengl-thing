package ecs.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ApplicationConfig {

    private ApplicationConfig() {}

    // Logger
    public static final Logger.Level LOGGER_SEVERITY = Logger.Level.TRACE;
    public static final String LOG_FILE_PATH = String.format(
            "C:\\Users\\Владислав\\Desktop\\java\\%s.txt",
            new SimpleDateFormat("yyyyMMdd'_'HHmmss").format(Date.from(Instant.now())));

    // Assets
    public static final String ASSETS_DIRECTORY_PATH = "assets\\";


}
