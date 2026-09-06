package sharks.lc5.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import java.util.List;

public class HomePage extends BasePage {

    private final String registerLink = "a.ico-register";
    private final String loginLink = "a.ico-login";
    private final String logoutLink = "a.ico-logout";
    private final String accountEmail = "a.account";
    private final String searchInput = "input[type='search']";
    private final String productTitleLocator = ".product-title";

    public HomePage(Page page) {
        super(page);
    }

    @Step("Click on 'Register' link in top navigation bar")
    public void clickRegister() {
        click(registerLink, "Register Link");
    }

    @Step("Click on 'Log in' link in top navigation bar")
    public void clickLogin() {
        click(loginLink, "Login Link");
    }

    @Step("Click on 'Log out' link")
    public void clickLogout() {
        click(logoutLink, "Logout Link");
    }

    @Step("Verify if user account header is displayed")
    public boolean isUserLoggedIn() {
        return isVisible(accountEmail);
    }

    @Step("Search for product: '{productName}'")
    public void searchProduct(String productName) {
        type(searchInput, productName, "Search Input");
        page.keyboard().press("Enter");
    }

    @Step("Check if product '{productName}' is listed in search results")
    public boolean isProductFound(String productName) {
        List<String> titles = getProductTitles(productTitleLocator);
        return isTitleMatching(titles, productName);
    }
}