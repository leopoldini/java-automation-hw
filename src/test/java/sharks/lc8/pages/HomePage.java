package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/**
 * The home page: only the top links are used - Register, Log in, Log out.
 *
 * The rule that keeps the chains working:
 *  - the action leads to another page -> return that page (openLoginPage returns LoginPage)
 *  - the user stays here -> return this
 * The return type always shows where the user is now.
 */
public class HomePage extends BasePage {

    // Selectors are constants at the top: if the site changes, I fix them only here
    private static final String REGISTER_LINK = "a.ico-register";
    private static final String LOGIN_LINK = "a.ico-login";
    private static final String LOGOUT_LINK = "a.ico-logout";

    // super(page): call the BasePage constructor and pass the tab to it
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

    // Only a logged-in user has the Log out link, so its presence is the check
    public boolean isUserLoggedIn() {
        return isVisible(LOGOUT_LINK);
    }
}