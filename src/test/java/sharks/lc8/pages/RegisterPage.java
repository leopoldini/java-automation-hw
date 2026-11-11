package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/* Форма регистрации (пол, имя, фамилия, email, пароли, кнопка Register). Идея Page Object описана в BasePage. */
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

    // В форме ДВА поля пароля (пароль + подтверждение), они должны совпадать, поэтому один метод заполняет оба.
    public RegisterPage fillPassword(String password) {
        type(PASSWORD, password);
        type(CONFIRM_PASSWORD, password);
        return this;
    }

    // После Register магазин открывает отдельную страницу "регистрация завершена",
    // поэтому возвращаем её Page Object: пользователь теперь на другой странице.
    public RegistrationResultPage submit() {
        click(REGISTER_BUTTON);
        return new RegistrationResultPage(page);
    }
}
