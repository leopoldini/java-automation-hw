package sharks.lc8.facade;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;
import sharks.lc8.config.ConfigManager;
import sharks.lc8.pages.HomePage;

/*
 Facade + Fluent, like AuthApiFacade, but for the browser.
 The test calls clear steps (openShop, registerNewUser, logout, loginAsRegisteredUser),
 and the Page Objects, selectors and checks are hidden inside.
 Remembers the email and password between steps, so it can log in later with the same user.
 */
public class ShopUiFacade {

    // The test gives the tab (BaseUiTest opened it): the facade does not open or close the browser itself
    private final Page page;
    private final ConfigManager config = ConfigManager.getInstance();

    // State between steps: the data of the registered user
    private String email;
    private String password;

    public ShopUiFacade(Page page) {
        this.page = page;
    }

    // @Step = the step name in the Allure report
    @Step("Open shop home page")
    public ShopUiFacade openShop() {
        // The shop address from config.properties (key baseUrl)
        page.navigate(config.getUiBaseUrl());
        return this;
    }

    @Step("Register new user")
    public ShopUiFacade registerNewUser() {
        email = generateEmail();
        password = config.getDefaultPassword();

        // A chain through three pages: HomePage -> RegisterPage -> RegistrationResultPage.
        // Every method returns the page the user is on now, so the chain does not break.
        String result = new HomePage(page)
                .openRegisterPage()
                .selectMaleGender()
                .fillFirstName("Leonid")
                .fillLastName("Tester")
                .fillEmail(email)
                .fillPassword(password)
                .submit()
                .getResultMessage();

        // The page only reports the text, the facade decides if it is correct
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
        // Login before registration (steps in the wrong order) -> a clear error right away, not null instead of the email
        Assert.assertNotNull(email, "User must be registered before login");

        boolean loggedIn = new HomePage(page)
                .openLoginPage()
                .fillEmail(email)
                .fillPassword(password)
                .submit()
                // The page reports true or false, the facade does the check
                .isUserLoggedIn();

        Assert.assertTrue(loggedIn, "User should be logged in");
        return this;
    }

    @Step("Login with invalid credentials")
    public ShopUiFacade loginWithInvalidCredentials() {
        boolean hasError = new HomePage(page)
                .openLoginPage()
                // This user surely does not exist: the time in milliseconds is new every time
                .fillEmail("no_such_user_" + System.currentTimeMillis() + "@test.com")
                .fillPassword("WrongPassword1!")
                // submitExpectingFailure() returns LoginPage, and submit() returns HomePage:
                // the type shows where the user ended up
                .submitExpectingFailure()
                .hasValidationError();

        Assert.assertTrue(hasError, "Validation error should be displayed");
        return this;
    }

    // A new email every time: the shop does not accept an already registered address
    private String generateEmail() {
        return "leonid" + System.currentTimeMillis() + "@testmail.com";
    }
}
