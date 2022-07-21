package ecs.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static ecs.utils.TerminalUtils.*;
import static ecs.utils.TerminalUtils.ANSI_RESET;

public class Logger {

    public enum Level {
        TRACE(0),
        DEBUG(1),
        INFO(2),
        WARN(3),
        ERROR(4),
        FATAL(5);

        private int priority;

        Level(int priority) {
            this.priority = priority;
        }
    }

    public interface Printer {
        void print(String message);
    }

    public static class ConsolePrinter implements Printer {
        @Override
        public void print(String message) {
            System.out.print(message);
        }
    }

    public static class OutputFilePrinter implements Printer {

        private OutputStream stream;

        public OutputFilePrinter(String filePath) {
            try {
                stream = new FileOutputStream(filePath);
            } catch (FileNotFoundException e) {
                error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        @Override
        public void print(String message) {
            try {
                stream.write(message.getBytes(StandardCharsets.ISO_8859_1));
            } catch (IOException e) {
                error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static final Level LEVEL = ApplicationConfig.LOGGER_SEVERITY;
    public static final String dateTimeFormatPattern = "HH:mm:ss.SSS";
    public static final Printer printer = new OutputFilePrinter(ApplicationConfig.LOG_FILE_PATH);

    private static final DateFormat dateFormat = new SimpleDateFormat(dateTimeFormatPattern);

    private static void printf(String level, String message) {
        printer.print(String.format(
                "\n" + ANSI_CYAN + "[%s]" + ANSI_RESET + " [%8s] [%s]: %s",
                dateFormat.format(Date.from(Instant.now())),
                Thread.currentThread().getName(),
                level,
                message
        ));
    }

    public static void log(Level level, String message) {
        if (level.priority < LEVEL.priority) return;
        printf(level.name(), message);
    }

    public static void trace(String message) {
        log(Level.TRACE, message);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message);
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void warn(String message) {
        log(Level.WARN, message);
    }

    public static void error(String message) {
        log(Level.ERROR, message);
    }

    public static void fatal(String message) {
        log(Level.FATAL, message);
    }
}
