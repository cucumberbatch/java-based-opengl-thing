package org.north.core.utils.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import static org.north.core.utils.TerminalUtils.Ansi.ANSI_BLACK;
import static org.north.core.utils.TerminalUtils.Ansi.ANSI_CYAN;

public class LoggerFactory {
    public static Logger createLogger(Class<?> scope) {
        Logger logger = Logger.getLogger(scope.getName());
        logger.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setFormatter(new CustomFormatter());
        handler.setErrorManager(new ErrorManager());
        logger.addHandler(handler);
        return logger;
    }

    private static class CustomFormatter extends Formatter {

        private static final SimpleDateFormat DATE_FORMAT =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        private static final String LOG_RECORD_PATTERN =
                ANSI_BLACK + "%1$s  %3$s [%2$8s] " + ANSI_CYAN + "%5$s" + ANSI_BLACK + ": %4$s\n";

        @Override
        public String format(LogRecord record) {
            return String.format(
                    LOG_RECORD_PATTERN,
                    DATE_FORMAT.format(Date.from(record.getInstant())),
                    Thread.currentThread().getName(),
                    record.getLevel(),
                    String.format(record.getMessage(), record.getParameters()),
                    record.getSourceClassName()
            );
        }
    }
}
