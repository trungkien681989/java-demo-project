package api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "api.properties";

    static {
        try (InputStream input = ApiConfig.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Configuration file not found: " + CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url", "https://reqres.in/api");
    }

    public static String getApiKey() {
        return properties.getProperty("api.key", "reqres_e75364f5445446809f80851af1fd9159");
    }

    public static String getApiKeyHeader() {
        return properties.getProperty("api.key.header", "x-api-key");
    }

    public static String getLoginEmail() {
        return properties.getProperty("api.login.email", "eve.holt@reqres.in");
    }

    public static String getLoginPassword() {
        return properties.getProperty("api.login.password", "cityslicka");
    }

    public static int getRequestTimeout() {
        return Integer.parseInt(properties.getProperty("api.request.timeout", "5000"));
    }

    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("api.connection.timeout", "10000"));
    }
}