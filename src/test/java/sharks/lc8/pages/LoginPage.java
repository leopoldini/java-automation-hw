package sharks.lc8.pages;

import com.microsoft.playwright.Page;

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

    public HomePage submit() {
        click(LOGIN_BUTTON);
        return new HomePage(page);
    }

    public LoginPage submitExpectingFailure() {
        click(LOGIN_BUTTON);
        return this;
    }

    public boolean hasValidationError() {
        return isVisible(VALIDATION_ERROR);
    }
}