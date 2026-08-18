package sharks.lc4.pages;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    private final String registerLink = "a.ico-register";
    private final String loginLink = "a.ico-login";
    private final String logoutLink = "a.ico-logout";
    private final String accountEmail = "div.header-links a.account";
    private final String searchInput = "input#small-searchterms";
    private final String searchButton = "input.button-1.search-box-button";
    private final String productTitles = "h2.product-title";

    public HomePage(Page page) {
        super(page);
    }

    public void clickRegister() {
        page.click(registerLink);
    }

    public void clickLogin() {
        page.click(loginLink);
    }

    public void clickLogout() {
        page.click(logoutLink);
    }

    public boolean isUserLoggedIn() {
        return page.isVisible(logoutLink);
    }

    public String getLoggedInAccountEmail() {
        return page.textContent(accountEmail);
    }

    public void searchProduct(String productName) {
        page.fill(searchInput, productName);
        page.click(searchButton);
    }

    public boolean isProductFound(String productName) {
        return page.locator(productTitles).allTextContents().stream()
                .anyMatch(title -> title.toLowerCase().contains(productName.toLowerCase()));
    }
}