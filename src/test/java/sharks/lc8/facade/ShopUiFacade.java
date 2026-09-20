package sharks.lc8.facade;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;
import sharks.lc8.config.ConfigManager;
import sharks.lc8.pages.HomePage;

/**
 * Same Facade + Fluent idea as AuthApiFacade, but for the browser.
 * The facade drives the page objects, so the test never knows which button sits on which page.
 *
 * Keeps the registered email and password, so a later step can log in as that same user.
 */
public class ShopUiFacade {

    // Page comes from the test - the facade does not open or close the browser
    private final Page page;
    private final ConfigManager config = ConfigManager.getInstance();

    private String email;
    private String password;

    public ShopUiFacade(Page page) {
        this.page = page;
    }

    @Step("Open shop home page")
    public ShopUiFacade openShop() {
        page.navigate(config.getUiBaseUrl());
        return this;
    }

    @Step("Register new user")
    public ShopUiFacade registerNewUser() {
        email = generateEmail();
        password = config.getDefaultPassword();

        // Fluent across three page objects: HomePage -> RegisterPage -> RegistrationResultPage.
        // Every method returns the page object where the user is now, that is what keeps the chain going.
        String result = new HomePage(page)
                .openRegisterPage()
                .selectMaleGender()
                .fillFirstName("Leonid")
                .fillLastName("Tester")
                .fillEmail(email)
                .fillPassword(password)
                .submit()
                .getResultMessage();

        Assert.assertTrue(result.contains("Your registration completed"),
                "Registration result: " + result);
        return this;
    }

    @Step("Logout")
    public ShopUiFacade logout() {
        new HomePage(page).logout();
        return this;
    }

    @Step("Login with registered user")
    public ShopUiFacade loginAsRegisteredUser() {
        // Clear message instead of a null password if the steps are chained in a wrong order
        Assert.assertNotNull(email, "User must be registered before login");

        boolean loggedIn = new HomePage(page)
                .openLoginPage()
                .fillEmail(email)
                .fillPassword(password)
                .submit()
                .isUserLoggedIn();

        Assert.assertTrue(loggedIn, "User should be logged in");
        return this;
    }

    @Step("Login with invalid credentials")
    public ShopUiFacade loginWithInvalidCredentials() {
        boolean hasError = new HomePage(page)
                .openLoginPage()
                .fillEmail("no_such_user_" + System.currentTimeMillis() + "@test.com")
                .fillPassword("WrongPassword1!")
                // submitExpectingFailure returns LoginPage, submit returns HomePage -
                // the return type shows where the user actually ends up
                .submitExpectingFailure()
                .hasValidationError();

        Assert.assertTrue(hasError, "Validation error should be displayed");
        return this;
    }

    // New email every run - the shop refuses an address that is already registered
    private String generateEmail() {
        return "leonid" + System.currentTimeMillis() + "@testmail.com";
    }
}