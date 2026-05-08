package com.axis.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration values from config.properties file.
 *
 * Uses static initializer block to load the file once
 * when the class is first accessed (lazy loading).
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    // Static initializer block - runs once when class is loaded
    static {
        loadProperties();
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class - use static methods only.
     */
    private ConfigReader() {
        throw new UnsupportedOperationException("ConfigReader is a utility class and cannot be instantiated");
    }

    /**
     * Loads properties from the config file.
     * Throws RuntimeException if the file cannot be loaded
     * because the framework cannot work without configuration.
     */
    private static void loadProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException(
                        "Configuration file not found: " + CONFIG_FILE +
                                ". Make sure it exists in src/test/resources/"
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE, e);
        }
    }

    /**
     * Gets a string value from config.
     *
     * @param key the property key
     * @return the property value
     * @throws RuntimeException if key not found
     */
    public static String get(String key) {
        String value = properties.getProperty(key);

        if (value == null) {
            throw new RuntimeException(
                    "Property not found: '" + key + "'. " +
                            "Check your config.properties file."
            );
        }

        return value.trim();
    }

    /**
     * Gets a string value with a default fallback.
     *
     * @param key the property key
     * @param defaultValue the value to return if key not found
     * @return the property value or default
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }

    /**
     * Gets an integer value from config.
     */
    public static int getInt(String key) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Property '" + key + "' is not a valid integer", e
            );
        }
    }

    /**
     * Gets a boolean value from config.
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}