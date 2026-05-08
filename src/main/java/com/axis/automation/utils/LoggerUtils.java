package com.axis.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility wrapper around Log4j2 for centralized logging.
 *
 * Provides simple static methods for logging without requiring
 * each class to create its own Logger instance.
 */
public final class LoggerUtils {

    private static final Logger logger = LogManager.getLogger("AutomationLogger");

    private LoggerUtils() {
        throw new UnsupportedOperationException("LoggerUtils is a utility class");
    }

    /**
     * Logs informational messages (test steps, navigation, actions).
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Logs debug messages (detailed technical information).
     * Only shown in DEBUG mode.
     */
    public static void debug(String message) {
        logger.debug(message);
    }

    /**
     * Logs warning messages (something unusual but not an error).
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * Logs error messages (something failed).
     */
    public static void error(String message) {
        logger.error(message);
    }

    /**
     * Logs error with exception stack trace.
     */
    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
