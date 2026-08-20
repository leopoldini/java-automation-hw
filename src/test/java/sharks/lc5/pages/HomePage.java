package sharks.lc5.pages;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    private final String registerLink = "a.ico-register";
    private final String loginLink = "a.ico-login";
    private final String logoutLink = "a.ico-logout";
    private final String searchInput = "#small-searchterms";
    private final String searchButton = "input.search-box-button";
    private final String productTitle = ".product-title";

    public HomePage(Page page) {
        super(page);
    }

    public void clickRegister() {
        click(registerLink, "Register Link");
    }

    public void clickLogin() {
        click(loginLink, "Login Link");
    }

    public void clickLogout() {
        click(logoutLink, "Logout Link");
    }

    public boolean isUserLoggedIn() {
        return isVisible(logoutLink);
    }

    public void searchProduct(String productName) {
        type(searchInput, productName, "Search Field");
        click(searchButton, "Search Button");
    }

    public boolean isProductFound(String productName) {
        return page.locator(productTitle).allInnerTexts().stream()
                .anyMatch(title -> title.toLowerCase().contains(productName.toLowerCase()));
    }
}