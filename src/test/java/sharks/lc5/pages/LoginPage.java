package sharks.lc5.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private final String emailInput = "#Email";
    private final String passwordInput = "#Password";
    private final String loginButton = "input.login-button";
    private final String errorMessage = ".validation-summary-errors";

    public LoginPage(Page page) {
        super(page);
    }

    public void login(String email, String password) {
        type(emailInput, email, "Email Field");
        type(passwordInput, password, "Password Field");
        click(loginButton, "Login Button");
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}