package sharks.lc8.facade;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;
import sharks.lc8.config.ConfigManager;
import sharks.lc8.pages.HomePage;

/*
 Facade + Fluent, как AuthApiFacade, только для браузера.
 Тест вызывает понятные шаги (openShop, registerNewUser, logout, loginAsRegisteredUser),
 а Page Objects, селекторы и проверки спрятаны внутри.
 Помнит email и пароль между шагами, чтобы потом войти этим же пользователем.
 */
public class ShopUiFacade {

    // Вкладку даёт тест (её открыл BaseUiTest): фасад сам браузер не открывает и не закрывает
    private final Page page;
    private final ConfigManager config = ConfigManager.getInstance();

    // Состояние между шагами: данные зарегистрированного пользователя
    private String email;
    private String password;

    public ShopUiFacade(Page page) {
        this.page = page;
    }

    // @Step = название шага в отчёте Allure
    @Step("Open shop home page")
    public ShopUiFacade openShop() {
        // Адрес магазина из config.properties (ключ baseUrl)
        page.navigate(config.getUiBaseUrl());
        return this;
    }

    @Step("Register new user")
    public ShopUiFacade registerNewUser() {
        email = generateEmail();
        password = config.getDefaultPassword();

        // Цепочка через три страницы: HomePage -> RegisterPage -> RegistrationResultPage.
        // Каждый метод возвращает страницу, на которой пользователь сейчас, поэтому цепочка не рвётся.
        String result = new HomePage(page)
                .openRegisterPage()
                .selectMaleGender()
                .fillFirstName("Leonid")
                .fillLastName("Tester")
                .fillEmail(email)
                .fillPassword(password)
                .submit()
                .getResultMessage();

        // Страница только сообщает текст, а правильный ли он, решает фасад
        Assert.assertTrue(result.contains("Your registration completed"),
                "Registration result: " + result);
        return this;
    }

    @Step("Logout")
    public ShopUiFacade logout() {
        new HomePage(page).logout();
        return this;
    }

    @Step("Login with registered user")
    public ShopUiFacade loginAsRegisteredUser() {
        // Логин до регистрации (шаги в неверном порядке) -> сразу понятная ошибка, а не null вместо email
        Assert.assertNotNull(email, "User must be registered before login");

        boolean loggedIn = new HomePage(page)
                .openLoginPage()
                .fillEmail(email)
                .fillPassword(password)
                .submit()
                // Страница сообщает true или false, а проверяет фасад
                .isUserLoggedIn();

        Assert.assertTrue(loggedIn, "User should be logged in");
        return this;
    }

    @Step("Login with invalid credentials")
    public ShopUiFacade loginWithInvalidCredentials() {
        boolean hasError = new HomePage(page)
                .openLoginPage()
                // Такого пользователя точно нет: время в миллисекундах каждый раз новое
                .fillEmail("no_such_user_" + System.currentTimeMillis() + "@test.com")
                .fillPassword("WrongPassword1!")
                // submitExpectingFailure() возвращает LoginPage, а submit() - HomePage:
                // тип показывает, где пользователь оказался
                .submitExpectingFailure()
                .hasValidationError();

        Assert.assertTrue(hasError, "Validation error should be displayed");
        return this;
    }

    // Новый email каждый раз: уже зарегистрированный адрес магазин не примет
    private String generateEmail() {
        return "leonid" + System.currentTimeMillis() + "@testmail.com";
    }
}
