package ecs.utils;

import ecs.config.ApplicationConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
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

    public static void log(Level level, String message, Throwable e) {
        if (level.compareTo(ApplicationConfig.LOGGER_SEVERITY) < 0) return;
        StringBuilder string = new StringBuilder();

        try {
            string.append(String.format(
                    "<cyan>%s</>  [%8s] %s: %s\n",
                    DATE_FORMAT.format(Date.from(Instant.now())),
                    Thread.currentThread().getName(),
                    level.formatLevel(),
                    message
            ));

            if (!Objects.isNull(e)) {
                warn("Not full stack trace visibility! If there is exception catching/rethrowing in trace then there can be not enough of error info!");
                string.append(String.format(
                        " [::] <yellow>%s</>\n in %s",
                        e.getClass().getName(),
                        Arrays.stream(e.getStackTrace())
                                .map(StackTraceElement::toString)
                                .collect(Collectors.joining("\n\t"))
                ));
            }

            writer.write(TerminalUtils.fAnsi(string.toString()));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    public static void log(Level level, String message) {
        log(level, message, null);
    }

    public static void log(Level level, Throwable e) {
        log(level, "", e);
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

    public static void error(Exception e) {
        log(Level.ERROR, e);
    }

    public static void error(String message, Exception e) {
        log(Level.ERROR, message, e);
    }

    public static void fatal(String message) {
        log(Level.FATAL, message);
    }

    public static void fatal(Exception e) {
        log(Level.FATAL, e);
    }

    public static void fatal(String message, Exception e) {
        log(Level.FATAL, message, e);
    }
}
