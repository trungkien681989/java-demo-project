package api.data;

import core.util.platform.environment.TestEnvironment;

public class ReqresApiTestData {

    // API Base URL from environment
    public static final String BASE_URL = TestEnvironment.getValue("api_base_url", "https://reqres.in/api");

    // API Key from environment
    public static final String API_KEY = TestEnvironment.getValue("api_key", "reqres_e75364f5445446809f80851af1fd9159");
    public static final String API_KEY_HEADER = TestEnvironment.getValue("api_key_header", "x-api-key");

    // Test Login Credentials
    public static final String LOGIN_EMAIL = TestEnvironment.getValue("api_login_email", "eve.holt@reqres.in");
    public static final String LOGIN_PASSWORD = TestEnvironment.getValue("api_login_password", "cityslicka");

    // API Timeout
    public static final int API_TIMEOUT = Integer.parseInt(TestEnvironment.getValue("api_timeout", "30"));

    // Invalid Credentials for negative tests
    public static final String INVALID_EMAIL = "invalid@reqres.in";
    public static final String INVALID_PASSWORD = "wrongpassword";
}
