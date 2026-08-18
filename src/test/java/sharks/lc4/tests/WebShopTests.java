package sharks.lc4.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc4.pages.HomePage;
import sharks.lc4.pages.LoginPage;
import sharks.lc4.pages.RegisterPage;

import java.util.UUID;

public class WebShopTests extends BaseTest {

    private final String userPassword = "Password123!";

    private String generateRandomEmail() {
        return "test_user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
    }

    private String registerNewUser() {
        String email = generateRandomEmail();
        HomePage homePage = new HomePage(page);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(page);
        registerPage.registerUser("Leonid", "QA", email, userPassword);
        return email;
    }

    @Test
    public void testSuccessfulRegistration() {
        String email = generateRandomEmail();

        HomePage homePage = new HomePage(page);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(page);
        registerPage.registerUser("Leonid", "QA", email, userPassword);

        Assert.assertTrue(registerPage.getRegistrationResultText().contains("Your registration completed"),
                "Registration should be successful");
    }

    @Test
    public void testSuccessfulLogin() {

        String email = registerNewUser();

        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(email, userPassword);

        Assert.assertTrue(homePage.isUserLoggedIn(), "Logout link should be visible after login");
    }

    @Test
    public void testLogout() {

        String email = registerNewUser();

        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(email, userPassword);

        homePage.clickLogout();

        Assert.assertFalse(homePage.isUserLoggedIn(), "Logout link should disappear after logout");
    }

    @Test
    public void testInvalidLogin() {
        HomePage homePage = new HomePage(page);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("non_existing_user_999@gmail.com", "WrongPassword!");

        Assert.assertTrue(loginPage.getErrorMessage().contains("Login was unsuccessful"),
                "Error message should be displayed for invalid credentials");
    }

    @Test
    public void testSearchProduct() {
        HomePage homePage = new HomePage(page);
        String searchItem = "Computer";

        homePage.searchProduct(searchItem);

        Assert.assertTrue(homePage.isProductFound(searchItem),
                "Search results should contain the product name: " + searchItem);
    }
}