package core.util.platform.environment;

import core.util.platform.host.file.YamlLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;
import java.util.Map;

public class TestEnvironment {
    private static final Logger LOGGER = LogManager.getLogger(TestEnvironment.class);
    private static final String ENVIRONMENT_OS_PROPERTY_KEY = "ENVIRONMENT";
    private static final SupportedEnvironment DEFAULT_ENVIRONMENT = SupportedEnvironment.TEST;

    private static SupportedEnvironment runningEnvironment;
    private static Map<String, String> settings;

    static {
        runningEnvironment = detectRunningEnvironment();
        settings = loadEnvironmentSettings();
    }

    private TestEnvironment() {
        // Private constructor to prevent instantiation
    }

    /**
     * Load environment settings from YAML file based on running environment
     */
    private static Map<String, String> loadEnvironmentSettings() {
        try {
            if (runningEnvironment == null) {
                LOGGER.error("Running environment is null");
                return Collections.emptyMap();
            }

            String configFileName = "core/environment/environment-" + runningEnvironment.toString().toLowerCase()
                    + ".yaml";
            Map<String, Object> loadedConfig = YamlLoader.loadConfig(configFileName);

            if (loadedConfig.isEmpty()) {
                LOGGER.error("Failed to load environment settings for: " + runningEnvironment);
                return Collections.emptyMap();
            }

            // Convert Map<String, Object> to Map<String, String>
            Map<String, String> convertedSettings = new java.util.HashMap<>();
            loadedConfig.forEach((key, value) -> convertedSettings.put(key, String.valueOf(value)));

            LOGGER.info("Environment settings loaded successfully for: " + runningEnvironment);
            return convertedSettings;
        } catch (Exception e) {
            LOGGER.error("Error loading environment settings: " + e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    /**
     * Get environment value by key
     */
    public static String getValue(String key) {
        if (key == null || key.trim().isEmpty()) {
            LOGGER.warn("Key is null or empty");
            return null;
        }

        String value = settings.get(key);
        if (value == null) {
            LOGGER.warn("Configuration key not found: " + key);
        }
        return value;
    }

    /**
     * Get environment value by key with default value
     */
    public static String getValue(String key, String defaultValue) {
        if (key == null || key.trim().isEmpty()) {
            LOGGER.warn("Key is null or empty, returning default value");
            return defaultValue;
        }

        String value = settings.getOrDefault(key, defaultValue);
        if (!value.equals(defaultValue)) {
            LOGGER.debug("Retrieved value for key '" + key + "': " + value);
        } else {
            LOGGER.debug("Using default value for key '" + key + "': " + defaultValue);
        }
        return value;
    }

    /**
     * Get the current running environment
     */
    public static SupportedEnvironment getRunningEnvironment() {
        if (runningEnvironment == null) {
            runningEnvironment = detectRunningEnvironment();
            LOGGER.warn("Running environment was null, re-detected: " + runningEnvironment);
        }
        return runningEnvironment;
    }

    /**
     * Detect running environment from system environment variable
     */
    private static SupportedEnvironment detectRunningEnvironment() {
        String environment = System.getenv(ENVIRONMENT_OS_PROPERTY_KEY);

        if (environment == null || environment.trim().isEmpty()) {
            LOGGER.warn("Environment variable '" + ENVIRONMENT_OS_PROPERTY_KEY + "' not set, using default: "
                    + DEFAULT_ENVIRONMENT);
            return DEFAULT_ENVIRONMENT;
        }

        SupportedEnvironment detectedEnvironment = SupportedEnvironment.getSupportedEnvironmentByName(environment);
        LOGGER.info("Detected environment from system property: " + detectedEnvironment);
        return detectedEnvironment;
    }

    /**
     * Get environment value for a specific environment (not the running one)
     */
    public static String getValueForEnvironment(String key, SupportedEnvironment environment) {
        if (key == null || key.trim().isEmpty()) {
            LOGGER.error("Key is null or empty");
            return null;
        }

        if (environment == null) {
            LOGGER.error("Environment is null");
            return null;
        }

        try {
            String configFileName = "core/environment/environment-" + environment.toString().toLowerCase() + ".yaml";
            Map<String, Object> loadedConfig = YamlLoader.loadConfig(configFileName);

            if (loadedConfig.isEmpty()) {
                LOGGER.error("Failed to load environment settings for: " + environment);
                return null;
            }

            Object value = loadedConfig.get(key);
            if (value == null) {
                LOGGER.warn("Key not found in environment '" + environment + "': " + key);
                return null;
            }

            LOGGER.debug("Retrieved value from environment '" + environment + "' for key '" + key + "': " + value);
            return String.valueOf(value);
        } catch (Exception e) {
            LOGGER.error("Error getting value for environment: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Supported environments enum
     */
    public enum SupportedEnvironment {
        DEV("DEV"),
        TEST("TEST"),
        UAT("UAT");

        private final String displayName;

        SupportedEnvironment(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        /**
         * Get SupportedEnvironment by name
         */
        public static SupportedEnvironment getSupportedEnvironmentByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                LOGGER.warn("Environment name is null or empty, using default: " + DEFAULT_ENVIRONMENT);
                return DEFAULT_ENVIRONMENT;
            }

            try {
                return SupportedEnvironment.valueOf(name.toUpperCase());
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Unsupported environment: " + name + ", using default: " + DEFAULT_ENVIRONMENT, e);
                return DEFAULT_ENVIRONMENT;
            }
        }
    }
}
