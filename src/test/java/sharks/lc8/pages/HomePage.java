package sharks.lc8.pages;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    private static final String REGISTER_LINK = "a.ico-register";
    private static final String LOGIN_LINK = "a.ico-login";
    private static final String LOGOUT_LINK = "a.ico-logout";
    private static final String ACCOUNT_LINK = "a.account";
    private static final String SEARCH_INPUT = "#small-searchterms";
    private static final String SEARCH_BUTTON = "input.button-1.search-box-button";

    public HomePage(Page page) {
        super(page);
    }

    public RegisterPage openRegisterPage() {
        click(REGISTER_LINK);
        return new RegisterPage(page);
    }

    public LoginPage openLoginPage() {
        click(LOGIN_LINK);
        return new LoginPage(page);
    }

    public HomePage logout() {
        click(LOGOUT_LINK);
        return this;
    }

    public HomePage searchFor(String query) {
        type(SEARCH_INPUT, query);
        click(SEARCH_BUTTON);
        return this;
    }

    public boolean isUserLoggedIn() {
        return isVisible(LOGOUT_LINK);
    }

    public String getAccountName() {
        return getText(ACCOUNT_LINK);
    }
}