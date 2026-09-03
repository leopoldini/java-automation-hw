package sharks.lc5.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class RegisterPage extends BasePage {

    private final String firstNameInput = "#FirstName";
    private final String lastNameInput = "#LastName";
    private final String emailInput = "#Email";
    private final String passwordInput = "#Password";
    private final String confirmPasswordInput = "#ConfirmPassword";
    private final String registerButton = "#register-button";
    private final String resultMessage = ".result";

    public RegisterPage(Page page) {
        super(page);
    }

    @Step("Register new user: '{firstName} {lastName}', Email: '{email}'")
    public void registerUser(String firstName, String lastName, String email, String password) {
        page.fill(firstNameInput, firstName);
        page.fill(lastNameInput, lastName);
        page.fill(emailInput, email);
        page.fill(passwordInput, password);
        page.fill(confirmPasswordInput, password);
        page.click(registerButton);
    }

    @Step("Get registration confirmation message")
    public String getRegistrationResultText() {
        return getText(resultMessage);
    }
}