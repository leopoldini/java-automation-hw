package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/* registration form (gender, names, e-mail, passwords, Register button). See BasePage for the Page Object idea. */
public class RegisterPage extends BasePage {

    private static final String GENDER_MALE = "#gender-male";
    private static final String FIRST_NAME = "#FirstName";
    private static final String LAST_NAME = "#LastName";
    private static final String EMAIL = "#Email";
    private static final String PASSWORD = "#Password";
    private static final String CONFIRM_PASSWORD = "#ConfirmPassword";
    private static final String REGISTER_BUTTON = "#register-button";
    private static final String RESULT_MESSAGE = ".result";

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

    // The form has TWO password fields (password + confirm) and they must match,
    // so one method fills both.
    public RegisterPage fillPassword(String password) {
        type(PASSWORD, password);
        type(CONFIRM_PASSWORD, password);
        return this;
    }

    // After pressing Register the shop shows the result on the SAME page, so we return this page.
    public RegisterPage submit() {
        click(REGISTER_BUTTON);
        return this;
    }

    // The message after registering, for example "Your registration completed".
    // innerText waits until the message appears on the page.
    public String getResultMessage() {
        return getText(RESULT_MESSAGE);
    }
}
