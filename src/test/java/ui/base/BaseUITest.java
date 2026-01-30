package ui.base;

import core.util.platform.environment.TestEnvironment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseUITest {
    protected WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 Starting UI Test");
        System.out.println("🌍 Environment: " + TestEnvironment.getRunningEnvironment());
        System.out.println("=".repeat(60));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        // Read headless mode from environment
        String headless = TestEnvironment.getValue("headless", "false");
        if ("true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless");
            System.out.println("🔧 Running in headless mode");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Browser closed");
        }
        System.out.println("=".repeat(60));
        System.out.println("✅ UI Test completed\n");
    }
}