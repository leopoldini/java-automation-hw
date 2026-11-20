package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/* The register form (gender, first name, last name, email, passwords, Register button). The Page Object idea is described in BasePage. */
public class RegisterPage extends BasePage {

    private static final String GENDER_MALE = "#gender-male";
    private static final String FIRST_NAME = "#FirstName";
    private static final String LAST_NAME = "#LastName";
    private static final String EMAIL = "#Email";
    private static final String PASSWORD = "#Password";
    private static final String CONFIRM_PASSWORD = "#ConfirmPassword";
    private static final String REGISTER_BUTTON = "#register-button";

    public RegisterPage(Page page) {
        super(page);
    }

    public RegisterPage selectMaleGender() {
        click(GENDER_MALE);
        return this;
    }

    public RegisterPage fillFirstName(String firstName) {
        type(FIRST_NAME, firstName);
        return this;
    }

    public RegisterPage fillLastName(String lastName) {
        type(LAST_NAME, lastName);
        return this;
    }

    public RegisterPage fillEmail(String email) {
        type(EMAIL, email);
        return this;
    }

    // The form has TWO password fields (password + confirmation), they must match, so one method fills both.
    public RegisterPage fillPassword(String password) {
        type(PASSWORD, password);
        type(CONFIRM_PASSWORD, password);
        return this;
    }

    // After Register the shop opens a separate "registration completed" page,
    // so we return its Page Object: the user is on another page now.
    public RegistrationResultPage submit() {
        click(REGISTER_BUTTON);
        return new RegistrationResultPage(page);
    }
}
