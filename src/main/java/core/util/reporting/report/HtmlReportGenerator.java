package core.util.reporting.report;

import core.util.platform.host.file.YamlLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static core.util.platform.host.file.FileHelper.copyDirectory;

public class HtmlReportGenerator {
    private static final Logger LOGGER = LogManager.getLogger(HtmlReportGenerator.class);

    // Configuration constants
    private static final String CONFIG_FILE_PATH = "core/reporting/extent-report.yaml";
    private static final String DATE_FORMAT = "yyyy_MM_dd";
    private static final String DATE_TIME_FORMAT = "yyyy_MM_dd___HH_mm_ss";

    // Cached configuration values
    private static Map<String, Object> extentReportSettings;
    private static String folderTestOutput;
    private static String folderNewestTestOutput;
    private static String screenshotsDirectory;
    private static int limitTimeToWaitForWriteReport;
    private static int pollingWaitForCheckReport;

    static {
        initializeConfiguration();
    }

    private HtmlReportGenerator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize configuration from YAML file
     */
    private static void initializeConfiguration() {
        try {
            extentReportSettings = YamlLoader.loadConfig(CONFIG_FILE_PATH);

            if (extentReportSettings.isEmpty()) {
                LOGGER.error("Failed to load extent report configuration from: " + CONFIG_FILE_PATH);
                setDefaultConfiguration();
                return;
            }

            folderTestOutput = getConfigString("folder_test_output", "/target/demo-reports/");
            folderNewestTestOutput = getConfigString("folder_newest_test_output", "/target/demo-reports/latest/");
            screenshotsDirectory = getConfigString("screenshots_directory", "screenshots/");
            limitTimeToWaitForWriteReport = getConfigInt("limit_wait_for_write_report", 30000);
            pollingWaitForCheckReport = getConfigInt("polling_wait_for_check_report", 500);

            LOGGER.info("Configuration loaded successfully");
        } catch (Exception e) {
            LOGGER.error("Error initializing configuration: " + e.getMessage(), e);
            setDefaultConfiguration();
        }
    }

    /**
     * Set default configuration values
     */
    private static void setDefaultConfiguration() {
        folderTestOutput = "/target/demo-reports/";
        folderNewestTestOutput = "/target/demo-reports/latest/";
        screenshotsDirectory = "screenshots/";
        limitTimeToWaitForWriteReport = 30000;
        pollingWaitForCheckReport = 500;
        LOGGER.info("Using default configuration");
    }

    /**
     * Get string configuration value with default
     */
    private static String getConfigString(String key, String defaultValue) {
        Object value = extentReportSettings.get(key);
        if (value == null) {
            LOGGER.warn("Configuration key not found: " + key + ", using default: " + defaultValue);
            return defaultValue;
        }
        return String.valueOf(value);
    }

    /**
     * Get integer configuration value with default
     */
    private static int getConfigInt(String key, int defaultValue) {
        Object value = extentReportSettings.get(key);
        if (value == null) {
            LOGGER.warn("Configuration key not found: " + key + ", using default: " + defaultValue);
            return defaultValue;
        }
        try {
            if (value instanceof Integer) {
                return (Integer) value;
            }
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid integer value for key '" + key + "': " + value + ", using default: " + defaultValue,
                    e);
            return defaultValue;
        }
    }

    /**
     * Create test output directory with date/time subdirectories and screenshots
     * folder
     */
    public static void createTestOutputDirectory() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT);
            SimpleDateFormat sdfDateTime = new SimpleDateFormat(DATE_TIME_FORMAT);

            String strDate = sdfDate.format(cal.getTime());
            String strTime = sdfDateTime.format(cal.getTime());
            String basePath = System.getProperty("user.dir");

            // Create main report directory
            String strReportDirectoryPath = basePath + folderTestOutput + strDate + File.separator + strTime
                    + File.separator;
            if (createDirectory(strReportDirectoryPath)) {
                System.setProperty("report.directory", strReportDirectoryPath);
                LOGGER.info("Report directory created: " + strReportDirectoryPath);
            } else {
                LOGGER.error("Failed to create report directory: " + strReportDirectoryPath);
            }

            // Create screenshots directory
            String strScreenshotDirectoryPath = basePath + folderTestOutput + strDate + File.separator + strTime
                    + File.separator + screenshotsDirectory;
            if (createDirectory(strScreenshotDirectoryPath)) {
                System.setProperty("screenshots.directory", strScreenshotDirectoryPath);
                LOGGER.info("Screenshots directory created: " + strScreenshotDirectoryPath);
            } else {
                LOGGER.error("Failed to create screenshots directory: " + strScreenshotDirectoryPath);
            }
        } catch (Exception e) {
            LOGGER.error("Error creating test output directory: " + e.getMessage(), e);
        }
    }

    /**
     * Create directory with null and existence checks
     */
    private static boolean createDirectory(String path) {
        if (path == null || path.isEmpty()) {
            LOGGER.error("Directory path is null or empty");
            return false;
        }

        File dir = new File(path);
        if (dir.exists()) {
            LOGGER.debug("Directory already exists: " + path);
            return true;
        }

        if (dir.mkdirs()) {
            return true;
        } else {
            LOGGER.error("Failed to create directory: " + path);
            return false;
        }
    }

    /**
     * Copy newest report to latest directory
     */
    public static void copyNewestReport() {
        try {
            String reportDirectory = System.getProperty("report.directory");
            if (reportDirectory == null || reportDirectory.isEmpty()) {
                LOGGER.error("Report directory property not set");
                return;
            }

            String strNewestTestOutput = System.getProperty("user.dir") + folderNewestTestOutput;
            File destFolder = new File(strNewestTestOutput);
            File srcFolder = new File(reportDirectory);

            if (!srcFolder.exists()) {
                LOGGER.error("Source folder does not exist: " + srcFolder.getAbsolutePath());
                return;
            }

            // Create destination folder if it doesn't exist
            if (!destFolder.exists() && !destFolder.mkdirs()) {
                LOGGER.error("Failed to create destination folder: " + destFolder.getAbsolutePath());
                return;
            }

            copyDirectory(srcFolder, destFolder);
            LOGGER.info("Report copied to: " + strNewestTestOutput);
        } catch (Exception e) {
            LOGGER.error("Error copying newest report: " + e.getMessage(), e);
        }
    }

    /**
     * Wait for report file to be written with retry logic
     */
    public static void waitForWriteReport(String path, String reportFileName) {
        if (path == null || path.isEmpty() || reportFileName == null || reportFileName.isEmpty()) {
            LOGGER.error("Path or report file name is null or empty");
            return;
        }

        try {
            String filePath = path + File.separator + reportFileName;
            int retryCount = 0;
            int maxRetries = limitTimeToWaitForWriteReport / pollingWaitForCheckReport;

            while (retryCount < maxRetries) {
                File reportFile = new File(filePath);
                if (reportFile.exists()) {
                    LOGGER.info("Report file created successfully: " + filePath);
                    return;
                }

                TimeUnit.MILLISECONDS.sleep(pollingWaitForCheckReport);
                retryCount++;

                if (retryCount % 10 == 0) {
                    LOGGER.debug("Waiting for report file... Attempt " + retryCount + "/" + maxRetries);
                }
            }

            LOGGER.warn("Report file not created within timeout period: " + filePath);
        } catch (InterruptedException ex) {
            LOGGER.error("Thread interrupted while waiting for report: " + ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.error("Error waiting for report file: " + e.getMessage(), e);
        }
    }
}
