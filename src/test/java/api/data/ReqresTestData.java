package api.data;

public class ReqresTestData {

    // Valid credentials
    public static final String VALID_EMAIL = "eve.holt@reqres.in";
    public static final String VALID_PASSWORD = "cityslicka";

    // Invalid credentials
    public static final String INVALID_EMAIL = "invalid@reqres.in";
    public static final String INVALID_PASSWORD = "wrongpassword";

    // Valid user IDs
    public static final int VALID_USER_ID = 2;
    public static final int VALID_USER_ID_2 = 3;

    // Invalid user IDs
    public static final int INVALID_USER_ID = 999;
    public static final String MALFORMED_USER_ID = "abc";

    // Valid user data for updates
    public static String getUpdateUserBody(String name, String job) {
        return "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"job\": \"" + job + "\"\n" +
                "}";
    }

    public static String getLoginBody(String email, String password) {
        return "{\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";
    }
}