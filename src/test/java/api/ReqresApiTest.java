package api;

import api.data.ReqresApiTestData;
import core.util.platform.environment.TestEnvironment;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ReqresApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ReqresApiTestData.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 Starting Reqres API Test Suite");
        System.out.println("🌍 Environment: " + TestEnvironment.getRunningEnvironment());
        System.out.println("🔗 Base URL: " + ReqresApiTestData.BASE_URL);
        System.out.println("=".repeat(60));
    }

    // ===================== LOGIN TESTS =====================

    @Test(description = "TC001: Login with valid credentials - Positive")
    public void testLoginWithValidCredentials() {
        System.out.println("\n📝 TC001: Login with valid credentials - Positive");
        String requestBody = "{\n" +
                "  \"email\": \"" + ReqresApiTestData.LOGIN_EMAIL + "\",\n" +
                "  \"password\": \"" + ReqresApiTestData.LOGIN_PASSWORD + "\"\n" +
                "}";

        given()
                .header("Content-Type", "application/json")
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC002: Login with missing email - Negative")
    public void testLoginWithMissingEmail() {
        System.out.println("\n📝 TC002: Login with missing email - Negative");
        String requestBody = "{\n" +
                "  \"password\": \"" + ReqresApiTestData.LOGIN_PASSWORD + "\"\n" +
                "}";

        given()
                .header("Content-Type", "application/json")
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC003: Login with missing password - Negative")
    public void testLoginWithMissingPassword() {
        System.out.println("\n📝 TC003: Login with missing password - Negative");
        String requestBody = "{\n" +
                "  \"email\": \"" + ReqresApiTestData.LOGIN_EMAIL + "\"\n" +
                "}";

        given()
                .header("Content-Type", "application/json")
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC004: Login with invalid credentials - Negative")
    public void testLoginWithInvalidCredentials() {
        System.out.println("\n📝 TC004: Login with invalid credentials - Negative");
        String requestBody = "{\n" +
                "  \"email\": \"" + ReqresApiTestData.INVALID_EMAIL + "\",\n" +
                "  \"password\": \"" + ReqresApiTestData.INVALID_PASSWORD + "\"\n" +
                "}";

        given()
                .header("Content-Type", "application/json")
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", notNullValue());
        System.out.println("✅ Test passed");
    }

    // ===================== GET USER TESTS =====================

    @Test(description = "TC005: Get existing user - Positive")
    public void testGetExistingUser() {
        System.out.println("\n📝 TC005: Get existing user - Positive");
        given()
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(1))
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC006: Get non-existent user - Negative")
    public void testGetNonExistentUser() {
        System.out.println("\n📝 TC006: Get non-existent user - Negative");
        given()
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .when()
                .get("/users/999")
                .then()
                .statusCode(404);
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC007: Get user list - Positive")
    public void testGetUserList() {
        System.out.println("\n📝 TC007: Get user list - Positive");
        given()
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .when()
                .get("/users?page=1")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("page", equalTo(1))
                .body("per_page", notNullValue())
                .body("total", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC008: Get user with invalid page - Negative")
    public void testGetUserWithInvalidPage() {
        System.out.println("\n📝 TC008: Get user with invalid page - Negative");
        given()
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .when()
                .get("/users?page=invalid")
                .then()
                .statusCode(200);
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC009: Get single user resource - Positive")
    public void testGetSingleUserResource() {
        System.out.println("\n📝 TC009: Get single user resource - Positive");
        given()
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .when()
                .get("/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.color", notNullValue())
                .body("data.name", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC010: Get paginated users - Positive")
    public void testGetPaginatedUsers() {
        System.out.println("\n📝 TC010: Get paginated users - Positive");
        given()
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", notNullValue());
        System.out.println("✅ Test passed");
    }

    // ===================== UPDATE USER TESTS =====================

    @Test(description = "TC011: Update user with valid data - Positive")
    public void testUpdateUserWithValidData() {
        System.out.println("\n📝 TC011: Update user with valid data - Positive");
        String updateBody = "{\n" +
                "  \"name\": \"Sam\",\n" +
                "  \"job\": \"Bui\"\n" +
                "}";

        given()
                .header("Content-Type", "application/json")
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .body(updateBody)
                .when()
                .put("/users/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Sam"))
                .body("job", equalTo("Bui"))
                .body("updatedAt", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC012: Update user with partial data - Positive")
    public void testUpdateUserWithPartialData() {
        System.out.println("\n📝 TC012: Update user with partial data - Positive");
        String updateBody = "{\n" +
                "  \"job\": \"QA Engineer\"\n" +
                "}";

        given()
                .header("Content-Type", "application/json")
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .body(updateBody)
                .when()
                .put("/users/3")
                .then()
                .statusCode(200)
                .body("job", equalTo("QA Engineer"))
                .body("updatedAt", notNullValue());
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC013: Update non-existent user - Negative")
    public void testUpdateNonExistentUser() {
        System.out.println("\n📝 TC013: Update non-existent user - Negative");
        String updateBody = "{\n" +
                "  \"name\": \"Ghost User\",\n" +
                "  \"job\": \"Developer\"\n" +
                "}";

        given()
                .header("Content-Type", "application/json")
                .header(ReqresApiTestData.API_KEY_HEADER, ReqresApiTestData.API_KEY)
                .accept("application/json")
                .body(updateBody)
                .when()
                .put("/users/999")
                .then()
                .statusCode(200)
                .body("updatedAt", notNullValue());
        System.out.println("✅ Test passed");
    }
}
