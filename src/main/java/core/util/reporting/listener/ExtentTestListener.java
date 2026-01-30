package core.util.reporting.listener;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import core.util.reporting.report.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class ExtentTestListener implements ITestListener {
    private static final Logger LOGGER = LogManager.getLogger(ExtentTestListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("Test Suite Finished: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        LOGGER.info("Test Started: " + testName);

        ExtentTest test = ExtentManager.createTest(testName, description != null ? description : "");
        test.info("Test execution started: " + testName);

        // Add test class and method info
        test.info("Test Class: " + result.getTestClass().getName());
        test.info("Test Method: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.info("Test Passed: " + testName);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel("Test Passed: " + testName, ExtentColor.GREEN));
            test.pass("Test execution completed successfully");
        }
        ExtentManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();

        LOGGER.error("Test Failed: " + testName, throwable);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel("Test Failed: " + testName, ExtentColor.RED));

            if (throwable != null) {
                test.fail("Exception: " + throwable.getMessage());
                test.fail("Stack Trace: <pre>" + getStackTrace(throwable) + "</pre>");
            }
        }
        ExtentManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.warn("Test Skipped: " + testName);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel("Test Skipped: " + testName, ExtentColor.YELLOW));

            if (result.getThrowable() != null) {
                test.skip("Reason: " + result.getThrowable().getMessage());
            }
        }
        ExtentManager.removeTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOGGER.info("Test Failed But Within Success Percentage: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.error("Test Failed With Timeout: " + testName);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel("Test Failed With Timeout: " + testName, ExtentColor.RED));
            if (result.getThrowable() != null) {
                test.fail("Exception: " + result.getThrowable().getMessage());
            }
        }
        ExtentManager.removeTest();
    }

    /**
     * Get formatted stack trace
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        Arrays.stream(throwable.getStackTrace())
                .limit(10)  // Limit stack trace to first 10 lines
                .forEach(element -> sb.append("\tat ").append(element.toString()).append("\n"));
        return sb.toString();
    }
}
