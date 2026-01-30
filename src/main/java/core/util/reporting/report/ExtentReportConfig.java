package core.util.reporting.report;

public enum ExtentReportConfig {
    EXTENT_REPORT_CONFIG_FILE_PATH("core/extent-report.yaml"),
    REPORT_NAME("reportName"),
    REPORT_CONFIG("reportConfig"),
    REPORT_DIRECTORY("reportDirectory"),
    FOLDER_TEST_OUTPUT("folderTestOutput"),
    FOLDER_NEWEST_TEST_OUTPUT("folderNewestTestOutput"),
    SCREENSHOTS_DIRECTORY("screenshotsDirectory"),
    LIMIT_WAIT_FOR_WRITE_REPORT("limitTimeToWaitForWriteReport"),
    POLLING_WAIT_FOR_CHECK_REPORT("pollingWaitForCheckReport");

    private String key;

    ExtentReportConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
