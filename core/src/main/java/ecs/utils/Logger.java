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

public class Logger {

    public enum Level {
        TRACE(0) {
            @Override
            public String formatLevel() {
                return enclose(name());
            }
        },
        DEBUG(1) {
            @Override
            public String formatLevel() {
                return String.format("<green>%s</>", enclose(name()));
            }
        },
        INFO(2) {
            @Override
            public String formatLevel() {
                return String.format("<blue>%s</>", enclose(name()));
            }
        },
        WARN(3) {
            @Override
            public String formatLevel() {
                return String.format("<yellow>%s</>", enclose(name()));
            }
        },
        ERROR(4) {
            @Override
            public String formatLevel() {
                return String.format("<red>%s</>", enclose(name()));
            }
        },
        FATAL(5) {
            @Override
            public String formatLevel() {
                return String.format("<Red>%s</>", enclose(name()));
            }
        };

        private int priority;

        public abstract String formatLevel();

        private static String enclose(String name) {
            return String.format("%7s", name);
        }

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
        private Printer attachedPrinter;

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
                stream.write(message.getBytes(StandardCharsets.UTF_8));
                attachedPrinter.print(message);
            } catch (IOException e) {
                error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        public OutputFilePrinter attach(Printer printer) {
            this.attachedPrinter = printer;
            return this;
        }
    }

    public static final Level LEVEL = ApplicationConfig.LOGGER_SEVERITY;

    public static final String dateTimeFormatPattern = "HH:mm:ss.SSS";
    public static final Printer printer = new ConsolePrinter();
    private static final DateFormat dateFormat = new SimpleDateFormat(dateTimeFormatPattern);

    private static void printf(Level level, String message) {
        printer.print(TerminalUtils.fAnsi(
                String.format(
                        "\n<cyan>%s</>  [%8s] %s: %s",
                        dateFormat.format(Date.from(Instant.now())),
                        Thread.currentThread().getName(),
                        level.formatLevel(),
                        message
                )
        ));
    }

    public static void log(Level level, String message) {
        if (level.priority < LEVEL.priority) return;
        printf(level, message);
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
