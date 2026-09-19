package sharks.lc8.facade;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;
import sharks.lc8.config.ConfigManager;
import sharks.lc8.pages.HomePage;

public class ShopUiFacade {

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
                .submitExpectingFailure()
                .hasValidationError();

        Assert.assertTrue(hasError, "Validation error should be displayed");
        return this;
    }

    @Step("Search for product")
    public ShopUiFacade searchProduct(String query) {
        new HomePage(page).searchFor(query);
        return this;
    }

    public String getEmail() {
        return email;
    }

    private String generateEmail() {
        return "leonid" + System.currentTimeMillis() + "@testmail.com";
    }
}