package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//*[@id='navbarDropdownMenuLink']/following-sibling::*[@class='dropdown-menu show']//*[@class='dropdown-item' and text()='Create Challenge']")
    private WebElement createChallengeButton;

    @FindBy(xpath = "//*[@id='navbarDropdownMenuLink']/following-sibling::*[@class='dropdown-menu show']//*[@class='dropdown-item' and contains(text(),'My')]")
    private WebElement myChallengesLink;

    @FindBy(xpath = "//*[@id='navbarDropdownMenuLink']/following-sibling::*[@data-toggle='dropdown']")
    private WebElement challengeDropdown;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(challengeDropdown));
            System.out.println("✅ Dashboard is displayed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Dashboard is not displayed");
            return false;
        }
    }

    public void clickChallengeDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(challengeDropdown));
        challengeDropdown.click();
        System.out.println("✅ Clicked Challenge dropdown");
    }

    public void clickCreateChallengeButton() {
        wait.until(ExpectedConditions.elementToBeClickable(createChallengeButton));
        createChallengeButton.click();
        System.out.println("✅ Clicked Create Challenge button");
    }

    public void clickMyChallengesLink() {
        wait.until(ExpectedConditions.elementToBeClickable(myChallengesLink));
        myChallengesLink.click();
        System.out.println("✅ Clicked My Challenges link");
    }
}