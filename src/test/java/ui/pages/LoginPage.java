package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(name = "identifier")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(), 'Login')]")
    private WebElement loginButton;

    @FindBy(xpath = "//a[contains(text(), 'Sign Up')]")
    private WebElement signUpLink;

    @FindBy(xpath = "//div[contains(@class, 'invalid-feedback')]")
    private WebElement alertMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navigateToLoginPage() {
        driver.get(ui.data.CTFLearnerTestData.BASE_URL + "/user/login");
        System.out.println("✅ Navigated to CTFLearner Login Page");
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
        System.out.println("✅ Entered username: " + username);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField));
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("✅ Entered password");
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        System.out.println("✅ Clicked Login button");
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("/dashboard"));
            System.out.println("✅ Login successful - Redirected to dashboard");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Login failed - Dashboard not reached");
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(alertMessage));
            return alertMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickSignUpLink() {
        wait.until(ExpectedConditions.elementToBeClickable(signUpLink));
        signUpLink.click();
        System.out.println("✅ Clicked Sign Up link");
    }
}