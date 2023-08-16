package ecs.utils;

import ecs.config.ApplicationConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Logger {

    public enum Level {
        TRACE {
            @Override
            public String formatLevel() {
                return enclose(name());
            }
        },
        DEBUG {
            @Override
            public String formatLevel() {
                return String.format("<blue>%s</>", enclose(name()));
            }
        },
        INFO{
            @Override
            public String formatLevel() {
                return String.format("<green>%s</>", enclose(name()));
            }
        },
        WARN {
            @Override
            public String formatLevel() {
                return String.format("<yellow>%s</>", enclose(name()));
            }
        },
        ERROR {
            @Override
            public String formatLevel() {
                return String.format("<red>%s</>", enclose(name()));
            }
        },
        FATAL {
            @Override
            public String formatLevel() {
                return String.format("<Red>%s</>", enclose(name()));
            }
        };

        public abstract String formatLevel();

        private static String enclose(String name) {
            return String.format("%7s", name);
        }

    }

    interface LogWriter {
        void write(String message) throws IOException;
        PrintWriter getPrintWriter();
    }

    public static class ConsoleLogWriter implements LogWriter {

        @Override
        public void write(String message) {
            System.out.print(message);
        }

        @Override
        public PrintWriter getPrintWriter() {
            return null;
        }
    }

    public static class FileLogWriter implements LogWriter {
        private final PrintWriter printWriter;
        private final OutputStream stream;

        public FileLogWriter(String fileName) {
            try {
                stream = new FileOutputStream(fileName);
                printWriter = new PrintWriter(stream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void write(String message) throws IOException {
            stream.write(message.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public PrintWriter getPrintWriter() {
            return printWriter;
        }
    }

    public static void setLogWriter(LogWriter logWriter) {
        writer = logWriter;

        // Appears in start of application
        // directPrint(ApplicationConfig.LOGGER_HEADER_TEXT);
    }

    public static LogWriter writer = new Logger.ConsoleLogWriter(); // console output by default

    public static final Level LEVEL = ApplicationConfig.LOGGER_SEVERITY;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(ApplicationConfig.LOGGER_TIME_INFO_PATTERN);

    private static void directPrint(String message) {
        try {
            writer.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static StringBuilder unwrapNestedException(Throwable throwable, StackTraceElement[] previousStackTrace) {
        if (Objects.isNull(throwable)) return new StringBuilder();
        List<StackTraceElement> previousStackTraceList = Arrays.stream(previousStackTrace).collect(Collectors.toList());
        String stackTraceFormat = "%s: %s%s\n" + (throwable.getCause() != null ? "Caused by: " : "");
        String className = throwable.getClass().getName();
        String message = throwable.getMessage();

        String stackTrace = Arrays.stream(throwable.getStackTrace())
                .filter(element -> !previousStackTraceList.contains(element))
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n    at ", "\n    at ", ""));

        return new StringBuilder(String.format(stackTraceFormat, className, message, stackTrace))
                .append(unwrapNestedException(throwable.getCause(), throwable.getStackTrace()));
    }

    public static void log(Level level, String message, Throwable throwable) {
        if (level.compareTo(ApplicationConfig.LOGGER_SEVERITY) < 0) return;
        StringBuilder string = new StringBuilder();

        try {
            string.append(String.format(
                    "<cyan>%s</>  [%8s] %s: %s\n%s",
                    DATE_FORMAT.format(Date.from(Instant.now())),
                    Thread.currentThread().getName(),
                    level.formatLevel(),
                    message,
                    unwrapNestedException(throwable, new StackTraceElement[]{})
            ));
            writer.write(TerminalUtils.fAnsi(string.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void log(Throwable throwable) {
        log(Level.ERROR, "Unhandled exception!", throwable);
    }

    public static void log(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    public static void log(Level level, String message) {
        log(level, message, null);
    }

    public static void log(Level level, Throwable throwable) {
        log(level, "", throwable);
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

    public static void error(Throwable e) {
        log(Level.ERROR, e);
    }

    public static void error(String message, Throwable e) {
        log(Level.ERROR, message, e);
    }

    public static void fatal(String message) {
        log(Level.FATAL, message);
    }

    public static void fatal(Exception e) {
        log(Level.FATAL, e);
    }

    public static void fatal(String message, Throwable e) {
        log(Level.FATAL, message, e);
    }
}
