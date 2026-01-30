package core.util.platform.host.file;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlLoader {
    private static final Logger LOGGER = LogManager.getLogger(YamlLoader.class);

    private YamlLoader() {
        // Private constructor to prevent instantiation
    }

    /**
     * Load YAML configuration file from classpath
     * 
     * @param filePath the relative path to the YAML file in resources
     * @return HashMap containing the loaded configuration, or empty HashMap if
     *         loading fails
     */
    public static Map<String, Object> loadConfig(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.error("File path is null or empty");
            return new HashMap<>();
        }

        try (InputStream inputStream = YamlLoader.class
                .getClassLoader()
                .getResourceAsStream(filePath)) {

            if (inputStream == null) {
                LOGGER.error("File not found in classpath: " + filePath);
                return new HashMap<>();
            }

            Yaml yaml = new Yaml();
            Map<String, Object> settings = yaml.load(inputStream);

            if (settings == null) {
                LOGGER.warn("YAML file is empty: " + filePath);
                return new HashMap<>();
            }

            LOGGER.info("Successfully loaded configuration from: " + filePath);
            return settings;
        } catch (Exception ex) {
            LOGGER.error("ERROR during loading config file: " + filePath, ex);
            return new HashMap<>();
        }
    }

    /**
     * Load YAML configuration file and get specific key
     * 
     * @param filePath the relative path to the YAML file
     * @param key      the key to retrieve from the configuration
     * @return the value associated with the key, or null if not found
     */
    public static Object getConfigValue(String filePath, String key) {
        if (key == null || key.trim().isEmpty()) {
            LOGGER.error("Key is null or empty");
            return null;
        }

        Map<String, Object> config = loadConfig(filePath);
        Object value = config.get(key);

        if (value == null) {
            LOGGER.warn("Key not found in configuration: " + key);
            return null;
        }

        LOGGER.debug("Retrieved value for key '" + key + "': " + value);
        return value;
    }

    /**
     * Load YAML configuration file and get nested value using dot notation
     * 
     * @param filePath the relative path to the YAML file
     * @param keyPath  the dot-separated path to the value (e.g., "database.host")
     * @return the value at the specified path, or null if not found
     */
    public static Object getNestedConfigValue(String filePath, String keyPath) {
        if (keyPath == null || keyPath.trim().isEmpty()) {
            LOGGER.error("Key path is null or empty");
            return null;
        }

        Map<String, Object> config = loadConfig(filePath);
        String[] keys = keyPath.split("\\.");
        Object current = config;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
                if (current == null) {
                    LOGGER.warn("Key not found in configuration path: " + keyPath);
                    return null;
                }
            } else {
                LOGGER.warn("Invalid configuration path: " + keyPath);
                return null;
            }
        }

        LOGGER.debug("Retrieved nested value for path '" + keyPath + "': " + current);
        return current;
    }

    /**
     * Load YAML configuration file and get string value
     * 
     * @param filePath     the relative path to the YAML file
     * @param key          the key to retrieve
     * @param defaultValue the default value if key is not found
     * @return the string value or default value if not found
     */
    public static String getConfigString(String filePath, String key, String defaultValue) {
        Object value = getConfigValue(filePath, key);
        if (value == null) {
            return defaultValue;
        }
        return String.valueOf(value);
    }

    /**
     * Load YAML configuration file and get integer value
     * 
     * @param filePath     the relative path to the YAML file
     * @param key          the key to retrieve
     * @param defaultValue the default value if key is not found or invalid
     * @return the integer value or default value if not found/invalid
     */
    public static int getConfigInt(String filePath, String key, int defaultValue) {
        Object value = getConfigValue(filePath, key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Invalid integer value for key '" + key + "': " + value, ex);
            return defaultValue;
        }
    }

    /**
     * Load YAML configuration file and get boolean value
     * 
     * @param filePath     the relative path to the YAML file
     * @param key          the key to retrieve
     * @param defaultValue the default value if key is not found
     * @return the boolean value or default value if not found
     */
    public static boolean getConfigBoolean(String filePath, String key, boolean defaultValue) {
        Object value = getConfigValue(filePath, key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }
}
