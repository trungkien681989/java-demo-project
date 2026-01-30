# Java Multi-Channel Test Automation Framework

A comprehensive test automation framework supporting Web UI, API, and Mobile testing with environment-based configuration and advanced reporting capabilities.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Quick Start](#quick-start)
- [Test Implementation](#test-implementation)
  - [CTFLearner Test Implementation](#ctflearner-test-implementation)
  - [API Test Implementation](#api-test-implementation)
  - [Mobile Test Implementation](#mobile-test-implementation)
- [Writing Tests](#writing-tests)
- [Test Reports](#test-reports)
- [Documentation](#documentation)

## Overview

This framework provides a unified solution for testing across multiple channels:
- **Web UI Testing**: Selenium WebDriver with Page Object Model
- **API Testing**: REST Assured for API automation
- **Mobile Testing**: Appium for Android/iOS testing
- **Environment Management**: YAML-based configuration for DEV/TEST/UAT environments
- **Reporting**: Extent Reports with screenshots and detailed test execution logs

## Features

- ✅ Page Object Model (POM) design pattern
- ✅ Environment-based configuration (DEV, TEST, UAT)
- ✅ Data-driven testing support
- ✅ Parallel test execution
- ✅ Comprehensive test reporting (Extent Reports, TestNG, Surefire)
- ✅ CI/CD integration ready
- ✅ Cross-browser testing support
- ✅ Mobile testing (Android/iOS)
- ✅ API testing with REST Assured

## Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Chrome/Firefox browser (for UI tests)
- Android SDK (for mobile tests)

### Installation

```bash
# Clone the repository
git clone <repository-url>
cd java-demo

# Install dependencies
mvn clean install

# Set environment (optional, defaults to DEV)
export ENVIRONMENT=TEST

# Run tests
mvn clean test
```

### Running Specific Test Suites

```bash
# Run CTFLearner UI tests
mvn clean test -Dtest=ui.CTFLearnerTest

# Run API tests
mvn clean test -Dtest=api.ReqresApiTest

# Run mobile tests
mvn clean test -Dtest=mobile.TradingAppAndroidTest

# Run with specific environment
export ENVIRONMENT=UAT
mvn clean test
```

## Test Implementation

### CTFLearner Test Implementation

The CTFLearner test suite provides comprehensive UI testing for the CTF Learning platform using Selenium WebDriver and the Page Object Model pattern.

#### Test Suite Overview

**Location**: [src/test/java/ui/CTFLearnerTest.java](src/test/java/ui/CTFLearnerTest.java)

**Test Coverage**: 6 test cases covering login, dashboard, challenge creation, and management

#### Architecture

```java
src/test/java/ui/
├── CTFLearnerTest.java              # Main test class
├── base/BaseUITest.java             # WebDriver setup and teardown
├── pages/                           # Page Object Model classes
│   ├── LoginPage.java              # Login page interactions
│   ├── DashboardPage.java          # Dashboard page interactions
│   ├── CreateChallengePage.java    # Create challenge page interactions
│   └── MyChallengesPage.java       # My challenges page interactions
└── data/CTFLearnerTestData.java    # Test data management
```

#### Test Cases

| Test ID | Description | Type | Priority |
|---------|-------------|------|----------|
| TC001 | Login with valid credentials | Positive | 1 |
| TC002 | Dashboard displayed after login | Positive | 2 |
| TC003 | Navigate to Create Challenge page | Positive | 3 |
| TC004 | Create challenge with existing title | Negative | 4 |
| TC005 | Create challenge with valid data | Positive | 5 |
| TC006 | View created challenge in My Challenges | Positive | 6 |

#### Page Object Model

**LoginPage** ([ui/pages/LoginPage.java](src/test/java/ui/pages/LoginPage.java))
- `navigateToLoginPage()`: Navigate to login page
- `login(username, password)`: Perform login operation
- `isLoginSuccessful()`: Verify successful login

**DashboardPage** ([ui/pages/DashboardPage.java](src/test/java/ui/pages/DashboardPage.java))
- `isDashboardDisplayed()`: Verify dashboard is visible
- `clickChallengeDropdown()`: Open challenge menu
- `clickCreateChallengeButton()`: Navigate to create challenge
- `clickMyChallengesLink()`: Navigate to my challenges

**CreateChallengePage** ([ui/pages/CreateChallengePage.java](src/test/java/ui/pages/CreateChallengePage.java))
- `isCreateChallengePageDisplayed()`: Verify page load
- `createChallenge(...)`: Create a new challenge
- `getErrorMessage()`: Retrieve validation errors

**MyChallengesPage** ([ui/pages/MyChallengesPage.java](src/test/java/ui/pages/MyChallengesPage.java))
- `isMyChallengesPageDisplayed()`: Verify page load
- `isChallengeVisible(title)`: Verify challenge exists

#### Test Data Management

**CTFLearnerTestData** ([ui/data/CTFLearnerTestData.java](src/test/java/ui/data/CTFLearnerTestData.java))
- Environment-aware configuration
- Valid/invalid credentials
- Challenge creation data
- Dynamic test data generation

#### Running CTFLearner Tests

```bash
# Run all CTFLearner tests
mvn clean test -Dtest=ui.CTFLearnerTest

# Run specific test
mvn clean test -Dtest=ui.CTFLearnerTest#testLoginWithValidCredentials

# Run with custom environment
export ENVIRONMENT=UAT
mvn clean test -Dtest=ui.CTFLearnerTest

# Run in headless mode
export headless=true
mvn clean test -Dtest=ui.CTFLearnerTest
```

#### Test Reports

After execution, view reports at:
- **Extent Report**: `target/demo-reports/latest/index.html`
- **TestNG Report**: `target/surefire-reports/index.html`
- **Logs**: `logs/test.log`

---

### API Test Implementation

The Reqres API test suite provides comprehensive REST API testing using REST Assured framework with data-driven approaches.

#### Test Suite Overview

**Location**: [src/test/java/api/ReqresApiTest.java](src/test/java/api/ReqresApiTest.java)

**Test Coverage**: 13 test cases covering authentication, user operations, and data retrieval

**Base URL**: https://reqres.in/api

#### Architecture

```java
src/test/java/api/
├── ReqresApiTest.java           # Main API test class
└── data/
    └── ReqresApiTestData.java   # API test data and configuration
```

#### Test Categories

##### 1. Login Tests (4 test cases)

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC001 | Login with valid credentials | Status 200, token returned |
| TC002 | Login with missing email | Status 400, error message |
| TC003 | Login with missing password | Status 400, error message |
| TC004 | Login with invalid credentials | Status 400, error message |

**Example Test**:
```java
@Test(description = "TC001: Login with valid credentials - Positive")
public void testLoginWithValidCredentials() {
    given()
        .header("Content-Type", "application/json")
        .body(loginRequestBody)
        .when()
        .post("/login")
        .then()
        .statusCode(200)
        .body("token", notNullValue());
}
```

##### 2. Get User Tests (6 test cases)

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC005 | Get existing user | Status 200, user data returned |
| TC006 | Get non-existent user | Status 404 |
| TC007 | Get user list | Status 200, paginated data |
| TC008 | Get user with invalid page | Status 200 (API handles gracefully) |
| TC009 | Get single user resource | Status 200, resource data |
| TC010 | Get paginated users | Status 200, page 2 data |

**Example Test**:
```java
@Test(description = "TC005: Get existing user - Positive")
public void testGetExistingUser() {
    given()
        .accept("application/json")
        .when()
        .get("/users/1")
        .then()
        .statusCode(200)
        .body("data.id", equalTo(1))
        .body("data.email", notNullValue());
}
```

##### 3. Update User Tests (3 test cases)

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC011 | Update user with valid data | Status 200, updated data returned |
| TC012 | Update user with partial data | Status 200, partial update applied |
| TC013 | Update non-existent user | Status 200 (API allows) |

**Example Test**:
```java
@Test(description = "TC011: Update user with valid data - Positive")
public void testUpdateUserWithValidData() {
    String updateBody = "{\"name\": \"Sam\", \"job\": \"Bui\"}";

    given()
        .header("Content-Type", "application/json")
        .body(updateBody)
        .when()
        .put("/users/1")
        .then()
        .statusCode(200)
        .body("name", equalTo("Sam"))
        .body("updatedAt", notNullValue());
}
```

#### API Test Data Configuration

**ReqresApiTestData** ([api/data/ReqresApiTestData.java](src/test/java/api/data/ReqresApiTestData.java))

```java
public class ReqresApiTestData {
    public static final String BASE_URL = "https://reqres.in/api";
    public static final String API_KEY_HEADER = "X-API-Key";
    public static final String API_KEY = "your-api-key";

    // Login credentials
    public static final String LOGIN_EMAIL = "eve.holt@reqres.in";
    public static final String LOGIN_PASSWORD = "cityslicka";

    // Invalid test data
    public static final String INVALID_EMAIL = "invalid@reqres.in";
    public static final String INVALID_PASSWORD = "wrongpassword";
}
```

#### Running API Tests

```bash
# Run all API tests
mvn clean test -Dtest=api.ReqresApiTest

# Run specific test category
mvn clean test -Dtest=api.ReqresApiTest#testLoginWithValidCredentials

# Run with environment configuration
export ENVIRONMENT=TEST
mvn clean test -Dtest=api.ReqresApiTest

# Enable detailed API logging
mvn clean test -Dtest=api.ReqresApiTest -Drestassured.debug=true
```

#### API Test Features

- **Request/Response Logging**: Automatic logging on validation failures
- **Environment Integration**: Uses TestEnvironment for configuration
- **Header Management**: Centralized API key and content-type headers
- **Hamcrest Matchers**: Rich assertion library for response validation
- **Test Isolation**: Each test is independent and can run in parallel

#### Test Reports

API test results include:
- **Request Details**: Method, URL, headers, body
- **Response Details**: Status code, headers, body, response time
- **Assertions**: Validation results with detailed error messages
- **Logs**: `logs/test.log` contains full API interaction logs

#### Best Practices

1. **Test Data Management**: Centralized in `ReqresApiTestData.java`
2. **Request Building**: Use `given()` for readable test structure
3. **Assertions**: Validate both status codes and response body
4. **Error Handling**: Test both positive and negative scenarios
5. **Logging**: Enable logging for debugging failed tests

---

### Mobile Test Implementation

The mobile test suite provides Android application testing using Appium framework.

#### Test Suite Overview

**Location**: [src/test/java/mobile/TradingAppAndroidTest.java](src/test/java/mobile/TradingAppAndroidTest.java)

#### Running Mobile Tests

```bash
# Start Appium server first
appium

# Run mobile tests
mvn clean test -Dtest=mobile.TradingAppAndroidTest
```

See [Mobile Testing Guide](docs/ENVIRONMENT_SETUP.md#mobile-configuration) for detailed setup.

## Writing Tests

### Creating a New UI Test

1. Create page object class in `src/test/java/ui/pages/`
2. Add test data to `src/test/java/ui/data/`
3. Create test class extending `BaseUITest`
4. Implement test methods with TestNG annotations

Example:
```java
@Test(description = "Verify login functionality", priority = 1)
public void testLogin() {
    loginPage = new LoginPage(driver);
    loginPage.login(username, password);
    Assert.assertTrue(loginPage.isLoginSuccessful());
}
```

### Creating a New API Test

1. Add test data to `src/test/java/api/data/ReqresApiTestData.java`
2. Create test method in `ReqresApiTest.java`
3. Use REST Assured fluent API

Example:
```java
@Test(description = "Create new user")
public void testCreateUser() {
    given()
        .header("Content-Type", "application/json")
        .body(requestBody)
        .when()
        .post("/users")
        .then()
        .statusCode(201);
}
```

## Test Reports

After test execution, reports are generated in:

- **Extent Reports**: `target/demo-reports/latest/index.html`
  - Rich HTML reports with screenshots
  - Test execution timeline
  - Environment information

- **TestNG Reports**: `target/surefire-reports/index.html`
  - Standard TestNG HTML reports

- **Surefire XML**: `target/surefire-reports/`
  - XML reports for CI/CD integration

- **Logs**: `logs/test.log`
  - Detailed test execution logs

## Documentation

Comprehensive documentation is available in the [docs/](docs/) directory:

- **[Documentation Index](docs/README.md)** - Central hub for all documentation
- **[Environment Setup Guide](docs/ENVIRONMENT_SETUP.md)** - Configuration details
- **[QA Strategy](docs/QA_STRATEGY.md)** - Complete QA approach and methodology
- **[QA Strategy Summary](docs/QA_STRATEGY_SUMMARY.md)** - Quick reference guide

### Key Documentation Sections

- [Environment Configuration](docs/ENVIRONMENT_SETUP.md#configuration-structure)
- [Test Automation Approach](docs/QA_STRATEGY.md#1-test-automation-solution)
- [CI/CD Integration](docs/QA_STRATEGY.md#cicd-pipeline-integration)
- [Performance Testing](docs/QA_STRATEGY.md#3-performance--security-assurance)

## Framework Architecture

```
java-demo/
├── src/
│   ├── main/java/core/              # Core framework
│   │   ├── controller/              # Custom annotations
│   │   ├── driver/                  # WebDriver management
│   │   └── util/
│   │       ├── platform/            # Environment, OS, mobile config
│   │       ├── scripting/           # Helpers (String, JSON, XML, Wait)
│   │       └── reporting/           # TestRail, Extent Reports
│   ├── main/resources/core/
│   │   └── environment/             # YAML configuration files
│   └── test/java/
│       ├── ui/                      # UI tests
│       ├── api/                     # API tests
│       └── mobile/                  # Mobile tests
├── docs/                            # Documentation
├── logs/                            # Test execution logs
└── target/                          # Build output and reports
```

## Environment Configuration

The framework supports multiple environments through YAML configuration:

```bash
# Set environment
export ENVIRONMENT=TEST  # Options: DEV, TEST, UAT

# Override specific values
export ctflearner_username="custom_user"
export headless="true"
```

Configuration files:
- `environment-dev.yaml` - Development environment
- `environment-test.yaml` - Test environment
- `environment-uat.yaml` - UAT environment

See [Environment Setup Guide](docs/ENVIRONMENT_SETUP.md) for details.

## Contributing

1. Follow the Page Object Model pattern
2. Write descriptive test names
3. Add test data to centralized data classes
4. Update documentation for new features
5. Ensure all tests pass before submitting PR

## Support

For issues or questions:
- Check [Documentation Index](docs/README.md)
- Review [Environment Setup Guide](docs/ENVIRONMENT_SETUP.md)
- Open an issue in the repository

---

**Version**: 1.0.0
**Last Updated**: January 2026
**Maintained By**: Sam Bui
