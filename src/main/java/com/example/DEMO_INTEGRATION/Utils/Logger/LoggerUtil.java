package com.example.DEMO_INTEGRATION.Utils.Logger;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.IllegalFormatException;

/**
 * Optimized Logger Utility class for standardized logging across the application.
 * Uses SLF4J with dynamic context, supports all standard log levels, and includes structured logging.
 */

public class LoggerUtil {

    private final Logger logger;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @FunctionalInterface
    interface LoggerWithThrowable {
        void log(Logger logger, String message, Throwable throwable);
    }

    private static final Map<LogLevel, BiConsumer<Logger, String>> LOG_METHODS = new HashMap<>();
    private static final Map<LogLevel, LoggerWithThrowable> LOG_WITH_THROWABLE = new HashMap<>();

    static {
        LOG_METHODS.put(LogLevel.ERROR, (l, m) -> l.error(m));
        LOG_METHODS.put(LogLevel.WARN, (l, m) -> l.warn(m));
        LOG_METHODS.put(LogLevel.INFO, (l, m) -> l.info(m));
        LOG_METHODS.put(LogLevel.DEBUG, (l, m) -> l.debug(m));
        LOG_METHODS.put(LogLevel.TRACE, (l, m) -> l.trace(m));

        LOG_WITH_THROWABLE.put(LogLevel.ERROR, (l, m, t) -> l.error(m, t));
        LOG_WITH_THROWABLE.put(LogLevel.WARN, (l, m, t) -> l.warn(m, t));
        LOG_WITH_THROWABLE.put(LogLevel.INFO, (l, m, t) -> l.info(m, t));
        LOG_WITH_THROWABLE.put(LogLevel.DEBUG, (l, m, t) -> l.debug(m, t));
        LOG_WITH_THROWABLE.put(LogLevel.TRACE, (l, m, t) -> l.trace(m, t));
    }

    private LoggerUtil(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public static LoggerUtil getLogger(Class<?> clazz) {
        return new LoggerUtil(clazz);
    }

    // Rest of the class remains unchanged
    public void log(LogLevel level, String message, Object[] args, Map<String, String> context) {
        if (!isLogLevelEnabled(level)) return;
        setContext(context);
        try {
            String formattedMessage = formatMessage(message, args);
            LOG_METHODS.getOrDefault(level, (l, m) -> {}).accept(logger, formattedMessage);
        } finally {
            MDC.clear();
        }
    }

    public void log(LogLevel level, String message, Throwable throwable, Object[] args, Map<String, String> context) {
        if (!isLogLevelEnabled(level)) return;
        setContext(context);
        try {
            String formattedMessage = formatMessage(message, args);
            LOG_WITH_THROWABLE.getOrDefault(level, (l, m, t) -> {}).log(logger, formattedMessage, throwable);
        } finally {
            MDC.clear();
        }
    }

    public void logStructured(LogLevel level, String message, Map<String, String> context) {
        if (!isLogLevelEnabled(level)) return;
        setContext(context);
        try {
            Map<String, Object> logEvent = new HashMap<>(context != null ? context : new HashMap<>());
            logEvent.put("message", message);
            logEvent.put("level", level.name());
            logEvent.put("timestamp", LocalDateTime.now().toString());
            logger.info(OBJECT_MAPPER.writeValueAsString(logEvent));
        } catch (Exception e) {
            logger.error("Failed to log structured message", e);
        } finally {
            MDC.clear();
        }
    }

    private void setContext(Map<String, String> context) {
        MDC.clear();
        if (context != null) {
            context.forEach(MDC::put);
        }
    }

    private String formatMessage(String message, Object[] args) {
        if (message == null) return "";
        if (args == null || args.length == 0) return message;
        try {
            return String.format(message, args);
        } catch (IllegalFormatException e) {
            return message + " [Formatting error: " + e.getMessage() + "]";
        }
    }

    private boolean isLogLevelEnabled(LogLevel level) {
        return switch (level) {
            case ERROR -> logger.isErrorEnabled();
            case WARN -> logger.isWarnEnabled();
            case INFO -> logger.isInfoEnabled();
            case DEBUG -> logger.isDebugEnabled();
            case TRACE -> logger.isTraceEnabled();
        };
    }

    public enum LogLevel {
        ERROR, WARN, INFO, DEBUG, TRACE
    }
}
