package sharks.lc4.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc4.pages.HomePage;
import sharks.lc4.pages.LoginPage;
import sharks.lc4.pages.RegisterPage;

import java.util.UUID;

public class WebShopTests extends BaseTest {

    private final String uniqueEmail = "test_user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
    private final String userPassword = "Password123!";

    @Test(priority = 1)
    public void testSuccessfulRegistration() {
        HomePage homePage = new HomePage(page);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(page);
        registerPage.registerUser("Leonid", "QA", uniqueEmail, userPassword);

        Assert.assertTrue(registerPage.getRegistrationResultText().contains("Your registration completed"),
                "Registration should be successful");
    }

    @Test(priority = 2)
    public void testSuccessfulLogin() {
        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(uniqueEmail, userPassword);

        Assert.assertTrue(homePage.isUserLoggedIn(), "Logout link should be visible after login");
    }

    @Test(priority = 3)
    public void testLogout() {
        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(uniqueEmail, userPassword);

        homePage.clickLogout();

        Assert.assertFalse(homePage.isUserLoggedIn(), "Logout link should disappear after logout");
    }

    @Test(priority = 4)
    public void testInvalidLogin() {
        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("invalid_email_12345@gmail.com", "WrongPassword!");

        Assert.assertTrue(loginPage.getErrorMessage().contains("Login was unsuccessful"),
                "Error message should be displayed for invalid credentials");
    }

    @Test(priority = 5)
    public void testSearchProduct() {
        HomePage homePage = new HomePage(page);
        String searchItem = "Computer";

        homePage.searchProduct(searchItem);

        Assert.assertTrue(homePage.isProductFound(searchItem),
                "Search results should contain the product name: " + searchItem);
    }
}