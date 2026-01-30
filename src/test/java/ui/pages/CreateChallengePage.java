package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreateChallengePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(name = "title")
    private WebElement titleField;

    @FindBy(name = "description")
    private WebElement descriptionField;

    @FindBy(name = "category")
    private WebElement categoryDropdown;

    @FindBy(name = "points")
    private WebElement pointsDropdown;

    @FindBy(name = "flag")
    private WebElement flagField;

    @FindBy(name = "howtosolve")
    private WebElement howToSolveField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[contains(@class, 'invalid-feedback')]")
    private WebElement alertMessage;

    public CreateChallengePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isCreateChallengePageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(titleField));
            System.out.println("✅ Create Challenge page is displayed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Create Challenge page is not displayed");
            return false;
        }
    }

    public void enterTitle(String title) {
        wait.until(ExpectedConditions.visibilityOf(titleField));
        titleField.clear();
        titleField.sendKeys(title);
        System.out.println("✅ Entered title: " + title);
    }

    public void enterDescription(String description) {
        wait.until(ExpectedConditions.visibilityOf(descriptionField));
        descriptionField.clear();
        descriptionField.sendKeys(description);
        System.out.println("✅ Entered description: " + description);
    }

    public void selectCategory(String category) {
        wait.until(ExpectedConditions.visibilityOf(categoryDropdown));
        Select select = new Select(categoryDropdown);
        select.selectByVisibleText(category);
        System.out.println("✅ Selected category: " + category);
    }

    public void selectPoints(String points) {
        wait.until(ExpectedConditions.visibilityOf(pointsDropdown));
        Select select = new Select(pointsDropdown);
        select.selectByVisibleText(points);
        System.out.println("✅ Selected points: " + points);
    }

    public void enterFlag(String flag) {
        wait.until(ExpectedConditions.visibilityOf(flagField));
        flagField.clear();
        flagField.sendKeys(flag);
        System.out.println("✅ Entered flag: " + flag);
    }

    public void enterHowToSolve(String howToSolve) {
        wait.until(ExpectedConditions.visibilityOf(howToSolveField));
        howToSolveField.clear();
        howToSolveField.sendKeys(howToSolve);
        System.out.println("✅ Entered how to solve: " + howToSolve);
    }

    public void clickSubmitButton() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        System.out.println("✅ Clicked Submit button");
    }

    public void createChallenge(String title, String description, String category, String points,
            String flag, String howToSolve) {
        enterTitle(title);
        enterDescription(description);
        selectCategory(category);
        selectPoints(points);
        enterFlag(flag);
        enterHowToSolve(howToSolve);
        clickSubmitButton();
    }

    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(alertMessage));
            return alertMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }
}