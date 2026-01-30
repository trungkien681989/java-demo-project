# Documentation Index

Welcome to the Java Multi-Channel Test Automation Framework documentation.

## 📚 Documentation Overview

### Getting Started

- **[Main README](../README.md)** - Project overview, quick start, and basic usage
- **[Environment Setup Guide](ENVIRONMENT_SETUP.md)** - Comprehensive guide to environment configuration

### QA Strategy & Best Practices

- **[QA Strategy (Full)](QA_STRATEGY.md)** - Complete QA strategy for online trading platform
  - Test Automation Solution
  - AI Integration in Testing
  - Performance & Security Assurance
  - Shift-Left Testing
- **[QA Strategy (Summary)](QA_STRATEGY_SUMMARY.md)** - Quick reference guide

## 📖 Documentation by Topic

### Test Automation

#### Web UI Testing

```java
// CTFLearner Test Suite
src/test/java/ui/
├── CTFLearnerTest.java       # Main test class
├── base/BaseUITest.java      # WebDriver setup
├── pages/                    # Page Object Model
│   ├── LoginPage.java
│   ├── DashboardPage.java
│   ├── CreateChallengePage.java
│   └── MyChallengesPage.java
└── data/CTFLearnerTestData.java
```

See: [README - CTFLearner Tests](../README.md#ctflearner-test-implementation)

#### API Testing

```java
// Reqres API Test Suite
src/test/java/api/
├── ReqresApiTest.java
└── data/ReqresApiTestData.java
```

See: [README - API Tests](../README.md#api-test-implementation)

#### Mobile Testing

```java
// Mobile App Test Suite
src/test/java/mobile/
└── TradingAppAndroidTest.java
```

### Environment Configuration

**Configuration Files:**

```
src/main/resources/core/environment/
├── environment-dev.yaml
├── environment-test.yaml
└── environment-uat.yaml
```

**Usage:**

```bash
export ENVIRONMENT=TEST
mvn clean test
```

See: [Environment Setup Guide](ENVIRONMENT_SETUP.md)

### Framework Architecture

**Core Framework:**

```
src/main/java/core/
├── controller/       # Custom annotations
├── driver/          # WebDriver management
└── util/
    ├── platform/    # Environment, OS, mobile config
    ├── scripting/   # Helpers (String, JSON, XML, Wait)
    └── reporting/   # TestRail, Extent Reports
```

## 🎯 Quick Navigation

### For Developers

1. [Quick Start Guide](../README.md#quick-start)
2. [Environment Configuration](ENVIRONMENT_SETUP.md)
3. [Writing Tests](../README.md#writing-tests)

### For QA Engineers

1. [QA Strategy Overview](QA_STRATEGY_SUMMARY.md)
2. [Test Automation Approach](QA_STRATEGY.md#1-test-automation-solution)
3. [Performance Testing](QA_STRATEGY.md#3-performance--security-assurance)

### For QA Managers

1. [QA Strategy Summary](QA_STRATEGY_SUMMARY.md)
2. [AI Integration](QA_STRATEGY.md#2-ai-integration-in-qa--testing)
3. [Shift-Left Testing](QA_STRATEGY.md#4-efficient-shift-left-testing)

### For DevOps Engineers

1. [CI/CD Integration](QA_STRATEGY.md#cicd-pipeline-integration)
2. [Environment Management](ENVIRONMENT_SETUP.md)
3. [Test Reports](../README.md#test-reports)

## 📊 Test Reports

**Report Locations:**

- Extent Reports: `/target/demo-reports/latest/`
- Surefire Reports: `/target/surefire-reports/`
- Logs: `logs/test.log`

## 🔧 Configuration Reference

### Test Execution

```bash
# Run all tests
mvn clean test

# Run specific test suite
mvn clean test -Dtest=ui.CTFLearnerTest
mvn clean test -Dtest=api.ReqresApiTest

# Run with environment
export ENVIRONMENT=UAT
mvn clean test

# Run with specific test suite file
mvn clean test -DsuiteXmlFile=src/test/resources/test_suites/test_suite_starter.xml
```

### Environment Variables

```bash
# Set test environment
export ENVIRONMENT=TEST  # DEV, TEST, UAT

# Override specific settings
export ctflearner_username="custom_user"
export headless="true"
```

See: [Environment Setup - Configuration Keys](ENVIRONMENT_SETUP.md#configuration-structure)

## 🏗️ Framework Components

### Page Object Model

- Separation of test logic and page interactions
- Reusable page components
- Environment-aware configuration
- Self-healing locator strategies

### Test Data Management

- Environment-specific test data
- Centralized data classes
- Dynamic data generation
- Data-driven testing support

### Reporting

- Extent Reports with screenshots
- TestNG HTML reports
- Surefire XML reports for CI/CD
- Real-time test execution logs

## 🤝 Contributing

### Code Standards

- Follow Page Object Model pattern
- Write descriptive test names
- Add comments for complex logic
- Update documentation for new features

### Testing Guidelines

- Minimum 80% code coverage
- All tests must pass before PR merge
- Run tests locally before pushing
- Add both positive and negative scenarios

## 📞 Support

### Getting Help

- Check [Environment Setup Guide](ENVIRONMENT_SETUP.md) for configuration issues
- Review [QA Strategy](QA_STRATEGY.md) for best practices
- Open an issue for bugs or feature requests

### Useful Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [REST Assured Guide](https://rest-assured.io/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Appium Documentation](https://appium.io/docs/en/latest/)

## 📝 Change Log

### Version 1.0.0 (Current)

- ✅ CTFLearner UI test suite (6 test cases)
- ✅ Reqres API test suite (13 test cases)
- ✅ Environment configuration system
- ✅ Page Object Model implementation
- ✅ CI/CD integration support
- ✅ Comprehensive QA strategy documentation

---

**Last Updated:** January 2026
**Maintained By:** Sam Bui
