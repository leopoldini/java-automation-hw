package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/**
 * Home page - only the top links are used: Register, Log in, Log out.
 *
 * Rule that makes the chains work:
 *  - action moves to another page -> return that page (openLoginPage returns LoginPage)
 *  - user stays here -> return this
 * So the return type always shows where you actually are.
 */
public class HomePage extends BasePage {

    // Selectors as constants at the top: if the site changes, this is the only place to fix
    private static final String REGISTER_LINK = "a.ico-register";
    private static final String LOGIN_LINK = "a.ico-login";
    private static final String LOGOUT_LINK = "a.ico-logout";

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

    // The Log out link only exists for a logged-in user, so its presence is the check itself
    public boolean isUserLoggedIn() {
        return isVisible(LOGOUT_LINK);
    }
}