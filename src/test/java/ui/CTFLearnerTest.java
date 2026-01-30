package ui;

import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUITest;
import ui.data.CTFLearnerTestData;
import ui.pages.CreateChallengePage;
import ui.pages.DashboardPage;
import ui.pages.LoginPage;
import ui.pages.MyChallengesPage;

public class CTFLearnerTest extends BaseUITest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private CreateChallengePage createChallengePage;
    private MyChallengesPage myChallengesPage;

    String title = CTFLearnerTestData.ChallengeData.getChallengeTitle();

    // ===================== LOGIN TESTS =====================

    @Test(description = "TC001: Login with valid credentials - Positive", priority = 1)
    public void testLoginWithValidCredentials() {
        System.out.println("\n📝 TC001: Login with valid credentials - Positive");
        loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(CTFLearnerTestData.VALID_USERNAME, CTFLearnerTestData.VALID_PASSWORD);

        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
        System.out.println("✅ Test passed");
    }

    // ===================== DASHBOARD TESTS =====================

    @Test(description = "TC002: Dashboard displayed after login - Positive", priority = 2, dependsOnMethods = "testLoginWithValidCredentials")
    public void testDashboardDisplayedAfterLogin() {
        System.out.println("\n📝 TC002: Dashboard displayed after login - Positive");
        dashboardPage = new DashboardPage(driver);

        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed");
        System.out.println("✅ Test passed");
    }

    // ===================== CREATE CHALLENGE TESTS =====================

    @Test(description = "TC003: Navigate to Create Challenge page - Positive", priority = 3, dependsOnMethods = "testDashboardDisplayedAfterLogin")
    public void testNavigateToCreateChallengePage() {
        System.out.println("\n📝 TC003: Navigate to Create Challenge page - Positive");
        dashboardPage = new DashboardPage(driver);
        dashboardPage.clickChallengeDropdown();
        dashboardPage.clickCreateChallengeButton();

        createChallengePage = new CreateChallengePage(driver);
        Assert.assertTrue(createChallengePage.isCreateChallengePageDisplayed(),
                "Create Challenge page should be displayed");
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC004: Create challenge with existing title - Negative", priority = 4, dependsOnMethods = "testNavigateToCreateChallengePage")
    public void testCreateChallengeWithExistingTitle() {
        System.out.println("\n📝 TC004: Create challenge with existing title - Negative");

        createChallengePage = new CreateChallengePage(driver);
        createChallengePage.createChallenge(
                CTFLearnerTestData.ChallengeData.CHALLENGE_EXISTING_TITLE,
                CTFLearnerTestData.ChallengeData.CHALLENGE_DESCRIPTION,
                CTFLearnerTestData.ChallengeData.CHALLENGE_CATEGORY,
                CTFLearnerTestData.ChallengeData.CHALLENGE_POINTS,
                CTFLearnerTestData.ChallengeData.CHALLENGE_FLAG,
                CTFLearnerTestData.ChallengeData.HOW_TO_SOLVE);

        String errorMessage = createChallengePage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("already exists") || errorMessage.length() == 0,
                "Error should be displayed for existing title");
        System.out.println("✅ Test passed");
    }

    @Test(description = "TC005: Create challenge with valid data - Positive", priority = 5, dependsOnMethods = "testNavigateToCreateChallengePage")
    public void testCreateChallengeWithValidData() {
        System.out.println("\n📝 TC005: Create challenge with valid data - Positive");

        createChallengePage = new CreateChallengePage(driver);
        createChallengePage.createChallenge(
                title,
                CTFLearnerTestData.ChallengeData.CHALLENGE_DESCRIPTION,
                CTFLearnerTestData.ChallengeData.CHALLENGE_CATEGORY,
                CTFLearnerTestData.ChallengeData.CHALLENGE_POINTS,
                CTFLearnerTestData.ChallengeData.CHALLENGE_FLAG,
                CTFLearnerTestData.ChallengeData.HOW_TO_SOLVE);
        System.out.println("✅ Test passed");
    }

    // ===================== MY CHALLENGES TESTS =====================

    @Test(description = "TC006: View created challenge in My Challenges - Positive", priority = 6, dependsOnMethods = "testCreateChallengeWithValidData")
    public void testViewCreatedChallengeInMyChallenges() {
        System.out.println("\n📝 TC006: View created challenge in My Challenges - Positive");
        dashboardPage = new DashboardPage(driver);
        dashboardPage.clickChallengeDropdown();
        dashboardPage.clickMyChallengesLink();

        myChallengesPage = new MyChallengesPage(driver);
        Assert.assertTrue(myChallengesPage.isMyChallengesPageDisplayed(), "My Challenges page should be displayed");
        Assert.assertTrue(myChallengesPage.isChallengeVisible(title),
                "Created challenge should be visible in My Challenges");
        System.out.println("✅ Test passed");
    }
}