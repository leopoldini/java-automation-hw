package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/**
 * Главная страница: используются только ссылки вверху - Register, Log in, Log out.
 *
 * Правило, на котором держатся цепочки:
 *  - действие уводит на другую страницу -> возвращаем её (openLoginPage возвращает LoginPage)
 *  - пользователь остался здесь -> return this
 * Тип результата всегда показывает, где пользователь сейчас.
 */
public class HomePage extends BasePage {

    // Селекторы - константы вверху: если сайт поменяется, правлю только здесь
    private static final String REGISTER_LINK = "a.ico-register";
    private static final String LOGIN_LINK = "a.ico-login";
    private static final String LOGOUT_LINK = "a.ico-logout";

    // super(page): вызвать конструктор BasePage и передать ему вкладку
    public HomePage(Page page) {
        super(page);
    }

    public RegisterPage openRegisterPage() {
        click(REGISTER_LINK);
        return new RegisterPage(page);
    }

    public LoginPage openLoginPage() {
        click(LOGIN_LINK);
        return new LoginPage(page);
    }

    public HomePage logout() {
        click(LOGOUT_LINK);
        return this;
    }

    // Ссылка Log out есть только у залогиненного пользователя, поэтому её наличие и есть проверка
    public boolean isUserLoggedIn() {
        return isVisible(LOGOUT_LINK);
    }
}