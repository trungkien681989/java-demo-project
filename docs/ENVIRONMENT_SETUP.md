# Environment Configuration Guide

## Overview

The framework supports multiple test environments (DEV, TEST, UAT) with environment-specific configurations managed through YAML files.

## Environment Files Location

```
src/main/resources/core/environment/
├── environment-dev.yaml
├── environment-test.yaml
└── environment-uat.yaml
```

## Configuration Structure

Each environment YAML file contains:

```yaml
# Environment name
environment_name: "TEST"

# CTFLearner Settings
ctflearner_url: "https://ctflearn.com"
ctflearner_username: "SamBui"
ctflearner_password: "Test@1234"

# API Settings
api_base_url: "https://reqres.in/api"
api_timeout: "30"

# Browser Settings
browser: "chrome"
headless: "false"
implicit_wait: "10"
page_load_timeout: "60"

# Test Data
test_data_path: "src/test/resources/test_data/test"
```

## How to Use Environment Settings

### 1. Set Environment Variable

**macOS/Linux:**
```bash
export ENVIRONMENT=TEST
```

**Windows (CMD):**
```cmd
set ENVIRONMENT=TEST
```

**Windows (PowerShell):**
```powershell
$env:ENVIRONMENT="TEST"
```

### 2. Run Tests with Environment

```bash
# Run with TEST environment (default)
mvn clean test -Dtest=ui.CTFLearnerTest

# Run with DEV environment
export ENVIRONMENT=DEV
mvn clean test -Dtest=ui.CTFLearnerTest

# Run with UAT environment
export ENVIRONMENT=UAT
mvn clean test -Dtest=ui.CTFLearnerTest
```

### 3. Access Environment Values in Code

**In Test Data Class:**
```java
import core.util.platform.environment.TestEnvironment;

public class CTFLearnerTestData {
    // Read from environment with default fallback
    public static final String BASE_URL = TestEnvironment.getValue("ctflearner_url", "https://ctflearn.com");
    public static final String VALID_USERNAME = TestEnvironment.getValue("ctflearner_username", "SamBui");
    public static final String VALID_PASSWORD = TestEnvironment.getValue("ctflearner_password", "Test@1234");
}
```

**In Test Class:**
```java
import core.util.platform.environment.TestEnvironment;

@Test
public void testExample() {
    String apiUrl = TestEnvironment.getValue("api_base_url");
    String timeout = TestEnvironment.getValue("api_timeout", "30");

    // Get current environment
    SupportedEnvironment env = TestEnvironment.getRunningEnvironment();
    System.out.println("Running in: " + env.getDisplayName());
}
```

**In Page Object:**
```java
import core.util.platform.environment.TestEnvironment;

public class LoginPage {
    private static final String BASE_URL = TestEnvironment.getValue("ctflearner_url");

    public void navigateToLoginPage() {
        driver.get(BASE_URL + "/user/login");
    }
}
```

## Environment-Specific Configuration

### DEV Environment
- **Purpose:** Development testing
- **Credentials:** dev_user / Dev@1234
- **Features:** Verbose logging, debug mode enabled

### TEST Environment (Default)
- **Purpose:** Standard test execution
- **Credentials:** SamBui / Test@1234
- **Features:** Balanced configuration for CI/CD

### UAT Environment
- **Purpose:** User acceptance testing
- **Credentials:** uat_user / Uat@1234
- **Features:** Production-like settings

## Adding New Configuration Keys

1. **Add to YAML files:**
```yaml
# In environment-test.yaml
new_feature_url: "https://test-feature.example.com"
new_feature_enabled: "true"
```

2. **Access in code:**
```java
String featureUrl = TestEnvironment.getValue("new_feature_url");
boolean isEnabled = Boolean.parseBoolean(TestEnvironment.getValue("new_feature_enabled", "false"));
```

## Common Use Cases

### 1. Different URLs per Environment
```java
String loginUrl = TestEnvironment.getValue("ctflearner_url") + "/user/login";
String apiUrl = TestEnvironment.getValue("api_base_url") + "/users";
```

### 2. Environment-Specific Test Data
```java
String testDataPath = TestEnvironment.getValue("test_data_path");
File testData = new File(testDataPath + "/users.json");
```

### 3. Conditional Test Execution
```java
@Test
public void testDevFeature() {
    if (TestEnvironment.getRunningEnvironment() == SupportedEnvironment.DEV) {
        // Only run in DEV environment
    }
}
```

### 4. Headless Browser Mode
```java
String headless = TestEnvironment.getValue("headless", "false");
if ("true".equalsIgnoreCase(headless)) {
    options.addArguments("--headless");
}
```

## CI/CD Integration

### Jenkins
```groovy
pipeline {
    environment {
        ENVIRONMENT = 'TEST'
    }
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
}
```

### GitHub Actions
```yaml
name: Run Tests
on: [push]
jobs:
  test:
    runs-on: ubuntu-latest
    env:
      ENVIRONMENT: TEST
    steps:
      - uses: actions/checkout@v2
      - name: Run tests
        run: mvn clean test
```

### GitLab CI
```yaml
test:
  variables:
    ENVIRONMENT: "TEST"
  script:
    - mvn clean test
```

## Best Practices

1. **Never commit credentials** - Use environment variables or secrets management
2. **Use default values** - Always provide fallback values
3. **Document all keys** - Keep this guide updated with new configuration keys
4. **Validate on startup** - Log environment detection for debugging
5. **Keep environments consistent** - Use same keys across all environment files

## Troubleshooting

### Environment Not Detected
```
WARNING: Environment variable 'ENVIRONMENT' not set, using default: TEST
```
**Solution:** Set the ENVIRONMENT variable before running tests

### Configuration Key Not Found
```
WARN: Configuration key not found: api_url
```
**Solution:** Add the key to the appropriate environment YAML file

### Wrong Environment Loaded
**Solution:** Check that ENVIRONMENT variable is set correctly:
```bash
echo $ENVIRONMENT  # macOS/Linux
echo %ENVIRONMENT% # Windows CMD
```

## Example: Complete Test Run

```bash
# Set environment
export ENVIRONMENT=TEST

# Verify environment is set
echo "Running in: $ENVIRONMENT"

# Run tests
mvn clean test -Dtest=ui.CTFLearnerTest

# Check logs for environment confirmation
# Look for: "🌍 Environment: TEST"
```

## Migration Guide

To migrate existing hardcoded values:

1. **Before:**
```java
String url = "https://ctflearn.com";
String username = "SamBui";
```

2. **After:**
```java
String url = TestEnvironment.getValue("ctflearner_url", "https://ctflearn.com");
String username = TestEnvironment.getValue("ctflearner_username", "SamBui");
```

This ensures backward compatibility while enabling environment-specific configuration.
