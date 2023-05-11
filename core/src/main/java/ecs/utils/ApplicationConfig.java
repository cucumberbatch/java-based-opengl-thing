package ecs.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class ApplicationConfig {

    private static final String APPLICATION_VERSION = "0.0.001";
    public static final String LOGGER_HEADER_TEXT = TerminalUtils.fAnsi("<blue>{ debug build }</>\n");
            /*"<Blue>" +

            "\n" +
            "\t███╗   ██╗ ██████╗ ██████╗ ████████╗██╗  ██╗\n" +
            "\t████╗  ██║██╔═══██╗██╔══██╗╚══██╔══╝██║  ██║\n" +
            "\t██╔██╗ ██║██║   ██║██████╔╝   ██║   ███████║\n" +
            "\t██║╚██╗██║██║   ██║██╔══██╗   ██║   ██╔══██║\n" +
            "\t██║ ╚████║╚██████╔╝██║  ██║   ██║   ██║  ██║\n" +
            "\t╚═╝  ╚═══╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝\n" +
            "\t                        game engine v" + APPLICATION_VERSION + "\n" +
            "\t                                   2020-" + LocalDate.now().getYear() + "\n" +

            "</>");*/

    private ApplicationConfig() {}

    // Logger
    public static final Logger.Level LOGGER_SEVERITY = Logger.Level.DEBUG;
    public static final String LOGGER_TIME_INFO_PATTERN = "HH:mm:ss.SSS";
    public static final String LOG_FILE_PATH = String.format(
            "C:\\Users\\Владислав\\Desktop\\java\\%s.log",
            new SimpleDateFormat("yyyyMMdd'_'HHmmss").format(Date.from(Instant.now())));

    // Assets
    public static final String ASSETS_DIRECTORY_PATH = "assets\\";

    public static final boolean DEVELOP = true;


}
