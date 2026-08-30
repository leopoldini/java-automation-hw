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
        page.click(registerLink);
    }

    @Step("Click on 'Log in' link in top navigation bar")
    public void clickLogin() {
        page.click(loginLink);
    }

    @Step("Click on 'Log out' link")
    public void clickLogout() {
        page.click(logoutLink);
    }

    @Step("Verify if user account header is displayed")
    public boolean isUserLoggedIn() {
        return page.isVisible(accountEmail);
    }

    @Step("Search for product: '{productName}'")
    public void searchProduct(String productName) {
        page.fill(searchInput, productName);
        page.keyboard().press("Enter");
    }

    @Step("Check if product '{productName}' is listed in search results")
    public boolean isProductFound(String productName) {
        List<String> titles = getProductTitles(productTitleLocator);
        return isTitleMatching(titles, productName);
    }
}