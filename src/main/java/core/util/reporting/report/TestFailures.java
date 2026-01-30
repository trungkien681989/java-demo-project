package core.util.reporting.report;

import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestFailures extends HashMap<ITestResult, List<Throwable>> {
    public static TestFailures getFailures() {
        if (failures == null) {
            failures = new TestFailures();
        }
        return failures;
    }

    public List<Throwable> getFailuresForTest(ITestResult result) {
        List<Throwable> exceptions = get(result);
        return exceptions == null ? new ArrayList<>() : exceptions;
    }

    public void addFailureForTest(ITestResult result, Throwable throwable) {
        List<Throwable> exceptions = getFailuresForTest(result);
        exceptions.add(throwable);
        put(result, exceptions);
    }

    private TestFailures() {
        super();
    }

    private static TestFailures failures;
}