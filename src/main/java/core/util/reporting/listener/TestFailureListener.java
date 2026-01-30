package core.util.reporting.listener;

import core.util.reporting.report.TestFailures;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

import java.util.List;

public class TestFailureListener implements IInvokedMethodListener {
    private static final Logger LOGGER = LogManager.getLogger(TestFailureListener.class);

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        LOGGER.info("Before invocation of " + method.getTestMethod().getMethodName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        LOGGER.info("After invocation of " + method.getTestMethod().getMethodName());
        Reporter.setCurrentTestResult(result);
        if (method.isTestMethod()) {
            TestFailures allFailures = TestFailures.getFailures();

            // Add an existing failure for the result to the failure list.
            if (result.getThrowable() != null) {
                allFailures.addFailureForTest(result, result.getThrowable());
            }

            List<Throwable> failures = allFailures.getFailuresForTest(result);
            // TODO Check logic why it loops the last failures
            if (failures.size() > 0) {
                failures.remove(failures.size() - 1);
            }
            int size = failures.size();
            LOGGER.info("Number of failed steps: " + size);

            if (size > 0) {
                result.setStatus(ITestResult.FAILURE);
                if (size == 1) {
                    result.setThrowable(failures.get(0));
                } else {
                    StringBuilder message = new StringBuilder("Mulitple failures (").append(size).append("):\n");
                    for (int i = 0; i < size - 1; i++) {
                        message.append("Failure ").append(i + 1).append(" of ").append(size).append("\n");
                        message.append(Utils.shortStackTrace(failures.get(i), false)).append("\n");
                    }
                    Throwable last = failures.get(size - 1);
                    message.append("Failure ").append(size).append(" of ").append(size).append("\n");
                    message.append(last.toString());
                    Throwable merged = new Throwable(message.toString());
                    merged.setStackTrace(last.getStackTrace());
                    result.setThrowable(merged);
                }
            }
        }
    }
}