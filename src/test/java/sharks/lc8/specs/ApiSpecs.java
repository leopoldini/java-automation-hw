package sharks.lc8.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import sharks.lc8.config.ConfigManager;

/**
 * Общие настройки для всех API-запросов (адрес, JSON, язык, логирование), чтобы не повторять их в каждом запросе.
 * Fluent: настройки собираются цепочкой вызовов RequestSpecBuilder через точку.
 * Utility class: final + private конструктор + только static методы. Данных нет, объекты не нужны (как у Math).
 */
public final class ApiSpecs {

    // Настройки берём из Singleton: адрес нигде не захардкожен, он лежит в config.properties.
    // static: static методы работают только со static полями.
    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    // private: объекты этого класса не нужны
    private ApiSpecs() {
    }

    // "Обычный бланк": для запросов без токена (register, login, guestSession)
    public static RequestSpecification base() {
        return new RequestSpecBuilder()
                .setBaseUri(CONFIG.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("localization", CONFIG.getLocalization())
                // Фильтры, а не .log(): печатают и запрос, и ответ. Без ответа в консоли упавший тест не разобрать.
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    // "Бланк с подписью": всё из base() + токен. Для запросов залогиненного пользователя (activatePromocode).
    // Токен приходит при регистрации или логине, AuthApiFacade хранит его и передаёт сюда через SlotCityApiClient.
    public static RequestSpecification authorized(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base())
                .addHeader("authorization", "Bearer " + token)
                .build();
    }
}