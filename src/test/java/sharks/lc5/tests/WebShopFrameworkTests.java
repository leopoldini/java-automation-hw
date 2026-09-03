package sharks.lc5.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sharks.lc5.config.ConfigManager;
import sharks.lc5.pages.HomePage;
import sharks.lc5.pages.LoginPage;
import sharks.lc5.pages.RegisterPage;
import sharks.lc5.providers.UserDataProvider;
import sharks.lc5.utils.DataGenerator;

@Epic("WebShop UI Framework")
@Feature("User Authentication & Catalog Search")
public class WebShopFrameworkTests extends BaseTest {

    private HomePage homePage;
    private RegisterPage registerPage;
    private LoginPage loginPage;

    private final String defaultPassword = "TestPassword123!";
    private final String SUCCESSFUL_REGISTRATION_MSG = "Your registration completed";
    private final String INVALID_LOGIN_MSG = "Login was unsuccessful";

    @BeforeMethod
    public void setUpPages() {
        context = com.microsoft.playwright.Playwright.create().chromium().launch().newContext();
        page = context.newPage();

        homePage = new HomePage(page);
        registerPage = new RegisterPage(page);
        loginPage = new LoginPage(page);

        page.navigate(ConfigManager.getProperty("baseUrl", "https://demowebshop.tricentis.com/"));
    }

    @Test
    @Story("User Registration")
    @Description("Verify that new user can successfully register with valid random data")
    public void testSuccessfulRegistration() {
        String email = DataGenerator.getRandomEmail();

        homePage.clickRegister();
        registerPage.registerUser(DataGenerator.getRandomFirstName(), DataGenerator.getRandomLastName(), email, defaultPassword);

        Assert.assertTrue(registerPage.getRegistrationResultText().contains(SUCCESSFUL_REGISTRATION_MSG));
    }

    @Test
    @Story("User Login")
    @Description("Verify that registered user can log in with valid credentials")
    public void testSuccessfulLogin() {
        String email = DataGenerator.getRandomEmail();

        homePage.clickRegister();
        registerPage.registerUser(DataGenerator.getRandomFirstName(), DataGenerator.getRandomLastName(), email, defaultPassword);
        homePage.clickLogout();

        homePage.clickLogin();
        loginPage.login(email, defaultPassword);

        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in successfully");
    }

    @Test(dataProvider = "invalidLoginData", dataProviderClass = UserDataProvider.class)
    @Story("User Login")
    @Description("Verify login failure with invalid credentials")
    public void testInvalidLogin(String email, String password) {
        homePage.clickLogin();
        loginPage.login(email, password);

        Assert.assertTrue(loginPage.getErrorMessage().contains(INVALID_LOGIN_MSG));
    }

    @Test(dataProvider = "searchData", dataProviderClass = UserDataProvider.class)
    @Story("Catalog Search")
    @Description("Verify catalog product search functionality")
    public void testSearchProducts(String productName) {
        homePage.searchProduct(productName);
        Assert.assertTrue(homePage.isProductFound(productName));
    }
}