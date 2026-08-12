package sharks.lc4.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private final String emailInput = "input#Email";
    private final String passwordInput = "input#Password";
    private final String loginButton = "input.button-1.login-button";
    private final String errorMessage = "div.validation-summary-errors";

    public LoginPage(Page page) {
        super(page);
    }

    public void login(String email, String password) {
        page.fill(emailInput, email);
        page.fill(passwordInput, password);
        page.click(loginButton);
    }

    public String getErrorMessage() {
        return page.textContent(errorMessage);
    }
}