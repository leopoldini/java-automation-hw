package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

/**
 * Интерфейс = "меню" запросов к серверу SlotCity: ЧТО можно сделать, без кода внутри.
 * Код написан в классах, которые его реализуют (implements):
 *  - SlotCityApiClient - настоящие HTTP-запросы;
 *  - LoggingApiClient - обёртка с логами (Decorator);
 *  - FakeApiClient в DecoratorTest - заготовленный ответ, без сервера.
 * Остальной код знает только меню, поэтому реализацию можно подменить, ничего больше не меняя.
 * (Как розетка: ей всё равно, что в неё включили - лампу или чайник.)
 *
 * Методы только отправляют запрос и возвращают сырой ответ (Response), ничего не проверяют.
 * Проверки - в фасаде AuthApiFacade.
 */
public interface ApiClient {

    Response register(UserDto user);

    Response login(UserDto user);

    Response guestSession();

    Response activatePromocode(String token, String promocode);
}
