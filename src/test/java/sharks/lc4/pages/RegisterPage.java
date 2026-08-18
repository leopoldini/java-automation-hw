package sharks.lc4.pages;

import com.microsoft.playwright.Page;

public class RegisterPage extends BasePage {

    private final String genderMaleRadio = "input#gender-male";
    private final String firstNameInput = "input#FirstName";
    private final String lastNameInput = "input#LastName";
    private final String emailInput = "input#Email";
    private final String passwordInput = "input#Password";
    private final String confirmPasswordInput = "input#ConfirmPassword";
    private final String registerButton = "input#register-button";
    private final String registrationResult = "div.result";

    public RegisterPage(Page page) {
        super(page);
    }

    public void registerUser(String firstName, String lastName, String email, String password) {
        page.click(genderMaleRadio);
        page.fill(firstNameInput, firstName);
        page.fill(lastNameInput, lastName);
        page.fill(emailInput, email);
        page.fill(passwordInput, password);
        page.fill(confirmPasswordInput, password);
        page.click(registerButton);
    }

    public String getRegistrationResultText() {
        return page.textContent(registrationResult);
    }
}