package sharks.lc8.pages;

import com.microsoft.playwright.Page;

public class RegisterPage extends BasePage {

    private static final String GENDER_MALE = "#gender-male";
    private static final String FIRST_NAME = "#FirstName";
    private static final String LAST_NAME = "#LastName";
    private static final String EMAIL = "#Email";
    private static final String PASSWORD = "#Password";
    private static final String CONFIRM_PASSWORD = "#ConfirmPassword";
    private static final String REGISTER_BUTTON = "#register-button";
    private static final String RESULT_MESSAGE = ".result";
    private static final String CONTINUE_BUTTON = "input.button-1.register-continue-button";

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

    public RegisterPage fillPassword(String password) {
        type(PASSWORD, password);
        type(CONFIRM_PASSWORD, password);
        return this;
    }

    public RegisterPage submit() {
        click(REGISTER_BUTTON);
        return this;
    }

    public HomePage continueAfterRegistration() {
        click(CONTINUE_BUTTON);
        return new HomePage(page);
    }

    public String getResultMessage() {
        return getText(RESULT_MESSAGE);
    }
}