package sharks.lc8.facade;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import sharks.lc8.api.ApiClient;
import sharks.lc8.api.SlotCityApiClient;
import sharks.lc8.config.ConfigManager;
import sharks.lc8.models.AuthResponseDto;
import sharks.lc8.models.GuestResponseDto;
import sharks.lc8.models.PromocodeResponseDto;
import sharks.lc8.models.UserDto;

/*
 Facade: простой вход перед сложной системой. Тест вызывает понятные бизнес-шаги
 (registerNewUser, switchToGuest, loginAsCurrentUser, activatePromocode),
 а Builder, клиент, DTO, токен и проверки спрятаны внутри.
 Fluent: каждый шаг возвращает this, поэтому шаги идут цепочкой через точку.
 Хранит состояние между шагами (пользователь + токен), поэтому каждому тесту нужен свой фасад (BaseApiTest).
 Проверки внутри шагов: если что-то сломалось, сообщение и шаг в Allure сразу показывают, где.
 */
public class AuthApiFacade {

    // Тип поля - интерфейс ApiClient. Чтобы включить логи, достаточно поменять эту строку
    // на new LoggingApiClient(new SlotCityApiClient()).
    private final ApiClient apiClient = new SlotCityApiClient();
    private final ConfigManager config = ConfigManager.getInstance();

    // Состояние между шагами: текущий пользователь и его токен
    private UserDto currentUser;
    private String token;

    // @Step = название шага в отчёте Allure
    @Step("Register new user")
    public AuthApiFacade registerNewUser() {
        // Builder. Email должен быть новым каждый раз: уже зарегистрированный сервер не примет.
        currentUser = UserDto.builder()
                .email(generateEmail())
                .password(config.getDefaultPassword())
                .build();

        Response response = apiClient.register(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Registration status code");

        // response.as() = Jackson превращает JSON в объект (десериализация)
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        // API отвечает 200 даже при ошибке, поэтому проверяем ещё и status в теле ответа
        Assert.assertTrue(auth.isStatus(), "Registration failed: " + response.asString());

        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after registration");
        return this;
    }

    // Выхода из аккаунта (logout) в API нет: /auth/guest выдаёт гостевой токен, и он заменяет токен пользователя.
    @Step("Switch to guest session")
    public AuthApiFacade switchToGuest() {
        Response response = apiClient.guestSession();
        Assert.assertEquals(response.getStatusCode(), 200, "Guest session status code");

        // Ответ плоский - {"token": "..."}, без объекта "user", поэтому отдельный DTO
        GuestResponseDto guest = response.as(GuestResponseDto.class);
        String guestToken = guest.getToken();

        Assert.assertNotNull(guestToken, "Guest token");
        // Доказывает, что мы действительно переключились: гостевой токен не может совпадать с токеном пользователя
        Assert.assertNotEquals(guestToken, token, "Guest token must differ from user token");

        token = guestToken;
        return this;
    }

    @Step("Login with current user")
    public AuthApiFacade loginAsCurrentUser() {
        // Логин до регистрации (шаги в неверном порядке) -> сразу понятная ошибка, а не NullPointerException
        Assert.assertNotNull(currentUser, "User must be registered before login");

        Response response = apiClient.login(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Login status code");

        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertTrue(auth.isStatus(), "Login failed: " + response.asString());

        // Логин выдаёт новый токен, он заменяет прежний (или гостевой)
        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after login");
        return this;
    }

    // Перегрузка: без параметра = код из config.properties, с параметром = любой код
    @Step("Activate promocode")
    public AuthApiFacade activatePromocode() {
        return activatePromocode(config.getPromocode());
    }

    @Step("Activate promocode {promocode}")
    public AuthApiFacade activatePromocode(String promocode) {
        Response response = apiClient.activatePromocode(token, promocode);
        Assert.assertEquals(response.getStatusCode(), 200, "Promocode activation status code");

        PromocodeResponseDto result = response.as(PromocodeResponseDto.class);
        // reason - объяснение сервера, например "expired promocode"
        Assert.assertTrue(result.isStatus(), "Promocode activation failed: " + result.getReason());
        return this;
    }

    // currentTimeMillis, а не Random: случайные числа начали совпадать с уже занятыми email
    private String generateEmail() {
        long suffix = 1000 + System.currentTimeMillis() % 1_000_000_000L;
        return "leopold+" + suffix + "@sharkscode.com";
    }
}