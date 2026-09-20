package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/* login form (e-mail, password, Log in button). See BasePage for the Page Object idea. */
public class LoginPage extends BasePage {

    private static final String EMAIL = "#Email";
    private static final String PASSWORD = "#Password";
    private static final String LOGIN_BUTTON = "input.button-1.login-button";
    private static final String VALIDATION_ERROR = ".validation-summary-errors";

    public LoginPage(Page page) {
        super(page);
    }

    public LoginPage fillEmail(String email) {
        type(EMAIL, email);
        return this;
    }

    public LoginPage fillPassword(String password) {
        type(PASSWORD, password);
        return this;
    }

    // Login SUCCEEDS: the shop sends us to the home page, so we return HomePage.
    public HomePage submit() {
        click(LOGIN_BUTTON);
        return new HomePage(page);
    }

    // Login FAILS (wrong data): we stay on the login form, so we return this page.
    public LoginPage submitExpectingFailure() {
        click(LOGIN_BUTTON);
        return this;
    }

    // The red error box that appears when the login data is wrong.
    public boolean hasValidationError() {
        return isVisible(VALIDATION_ERROR);
    }
}
