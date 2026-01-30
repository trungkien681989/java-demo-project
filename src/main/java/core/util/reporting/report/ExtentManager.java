package core.util.reporting.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.util.platform.host.file.YamlLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;

public class ExtentManager {
    private static final Logger LOGGER = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static final String CONFIG_FILE_PATH = "core/extent-report.yaml";

    private ExtentManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize ExtentReports with configuration
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    /**
     * Create ExtentReports instance
     */
    private static synchronized void createInstance() {
        try {
            // Load configuration
            Map<String, Object> config = YamlLoader.loadConfig(CONFIG_FILE_PATH);

            String reportDirectory = System.getProperty("report.directory");
            String reportName = config.getOrDefault("reportName", "Demo_Test_Report.html").toString();
            String reportConfigPath = config.getOrDefault("reportConfig", "/src/main/resources/core/extent-config.xml").toString();

            if (reportDirectory == null || reportDirectory.isEmpty()) {
                LOGGER.error("Report directory is not set. Call HtmlReportGenerator.createTestOutputDirectory() first.");
                reportDirectory = System.getProperty("user.dir") + "/target/demo-reports/";
            }

            String reportPath = reportDirectory + File.separator + reportName;
            LOGGER.info("Creating ExtentReports at: " + reportPath);

            // Create ExtentSparkReporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

            // Load XML configuration if exists
            String configFilePath = System.getProperty("user.dir") + reportConfigPath;
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                sparkReporter.loadXMLConfig(configFile);
                LOGGER.info("Loaded ExtentReports XML configuration from: " + configFilePath);
            } else {
                LOGGER.warn("Configuration file not found at: " + configFilePath + ", using default settings");
                sparkReporter.config().setTheme(Theme.DARK);
                sparkReporter.config().setDocumentTitle("Demo Test Report");
                sparkReporter.config().setReportName("Demo Test Report");
            }

            // Create ExtentReports and attach reporter
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Set system info
            extent.setSystemInfo("Environment", System.getProperty("env", "QA"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            LOGGER.info("ExtentReports initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize ExtentReports: " + e.getMessage(), e);
        }
    }

    /**
     * Create a test in the report
     */
    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        extentTest.set(test);
        return test;
    }

    /**
     * Get the current test
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Remove the current test from ThreadLocal
     */
    public static void removeTest() {
        extentTest.remove();
    }

    /**
     * Flush the report
     */
    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
            LOGGER.info("ExtentReports flushed successfully");
        }
    }
}
