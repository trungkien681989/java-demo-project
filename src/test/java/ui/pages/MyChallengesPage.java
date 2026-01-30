package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MyChallengesPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//*[contains(@class,'card-header')]")
    private List<WebElement> challengeHeaders;

    @FindBy(xpath = "//h1[contains(text(),'Challenges by')]")
    private WebElement pageTitle;

    public MyChallengesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isMyChallengesPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            System.out.println("✅ My Challenges page is displayed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ My Challenges page is not displayed");
            return false;
        }
    }

    public boolean isChallengeVisible(String challengeTitle) {
        try {
            By challengeLocator = By.xpath("//*[contains(@class,'card-header')]//*[text()='" + challengeTitle + "']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(challengeLocator));
            System.out.println("✅ Challenge found: " + challengeTitle);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Challenge not found: " + challengeTitle);
            return false;
        }
    }

    public int getChallengeCount() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        int count = challengeHeaders.size();
        System.out.println("✅ Total challenges: " + count);
        return count;
    }
}