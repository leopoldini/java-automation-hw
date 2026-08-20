package sharks.lc5.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc5.pages.HomePage;
import sharks.lc5.pages.LoginPage;
import sharks.lc5.pages.RegisterPage;
import sharks.lc5.providers.UserDataProvider;
import sharks.lc5.utils.DataGenerator;
import sharks.lc5.utils.LoggerUtil;

public class WebShopFrameworkTests extends BaseTest {

    private static final String SUCCESSFUL_REGISTRATION_MSG = "Your registration completed";
    private static final String INVALID_LOGIN_MSG = "Login was unsuccessful";
    private final String defaultPassword = "Password123!";

    private String registerNewUser() {
        String email = DataGenerator.getRandomEmail();
        HomePage homePage = new HomePage(page);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(page);
        registerPage.registerUser(DataGenerator.getRandomFirstName(), DataGenerator.getRandomLastName(), email, defaultPassword);
        homePage.clickLogout();
        return email;
    }

    @Test
    public void testSuccessfulRegistration() {
        LoggerUtil.info("Starting testSuccessfulRegistration...");
        String email = DataGenerator.getRandomEmail();

        HomePage homePage = new HomePage(page);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(page);
        registerPage.registerUser(DataGenerator.getRandomFirstName(), DataGenerator.getRandomLastName(), email, defaultPassword);

        Assert.assertTrue(registerPage.getRegistrationResultText().contains(SUCCESSFUL_REGISTRATION_MSG),
                "Registration should be successful");
    }

    @Test
    public void testSuccessfulLogin() {
        LoggerUtil.info("Starting testSuccessfulLogin...");
        String email = registerNewUser();

        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(email, defaultPassword);

        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in successfully");
    }

    @Test(dataProvider = "invalidLoginData", dataProviderClass = UserDataProvider.class)
    public void testInvalidLogin(String email, String password) {
        LoggerUtil.info("Starting testInvalidLogin for email: " + email);
        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(email, password);

        Assert.assertTrue(loginPage.getErrorMessage().contains(INVALID_LOGIN_MSG),
                "Error message should be displayed");
    }

    @Test(dataProvider = "searchProducts", dataProviderClass = UserDataProvider.class)
    public void testSearchProducts(String productName) {
        LoggerUtil.info("Starting testSearchProducts for item: " + productName);
        HomePage homePage = new HomePage(page);
        homePage.searchProduct(productName);

        Assert.assertTrue(homePage.isProductFound(productName),
                "Search results should contain: " + productName);
    }
}