package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.LoginRequestDto;
import sharks.lc8.models.PromocodeRequestDto;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;
import sharks.lc8.specs.ApiSpecs;

import static io.restassured.RestAssured.given;

/**
 * Настоящий API-клиент: отправляет HTTP-запросы на сервер SlotCity через Rest Assured.
 * Единственный класс, который знает пути (/auth/login и т.д.): поменяется путь - правлю только здесь.
 *
 * Каждый метод читается как предложение:
 *   given()  = "дано": общие настройки (spec) + тело запроса (body)
 *   when()   = "когда": HTTP-метод и путь (post / get)
 *   then().extract().response() = "тогда": просто забрать ответ, проверки - в фасаде
 */
public class SlotCityApiClient implements ApiClient {

    // @Override: метод из "меню" ApiClient - то же имя и те же параметры, а тело своё (настоящий запрос).
    // Ошибусь в имени или параметрах - IDEA покажет ошибку.
    @Override
    public Response register(UserDto user) {
        return given()
                .spec(ApiSpecs.base())
                // Передаём обычный Java-объект, Rest Assured сам превращает его в JSON (сериализация).
                // from() переводит UserDto в формат сервера.
                .body(RegisterRequestDto.from(user))
                .when()
                // Только путь: адрес сервера уже в ApiSpecs.base()
                .post("/auth/v2/register?on_device=true")
                .then()
                .extract()
                .response();
    }

    @Override
    public Response login(UserDto user) {
        return given()
                .spec(ApiSpecs.base())
                .body(LoginRequestDto.from(user))
                .when()
                .post("/auth/login?on_device=true")
                .then()
                .extract()
                .response();
    }

    // GET и без body: отправлять нечего, только получаем гостевой токен
    @Override
    public Response guestSession() {
        return given()
                .spec(ApiSpecs.base())
                .when()
                .get("/auth/guest")
                .then()
                .extract()
                .response();
    }

    @Override
    public Response activatePromocode(String token, String promocode) {
        return given()
                // authorized = обычные настройки + токен в заголовке: сервер понимает, КТО активирует промокод
                .spec(ApiSpecs.authorized(token))
                .body(PromocodeRequestDto.builder().code(promocode).build())
                .when()
                .post("/apiv2/promocodes/activate")
                .then()
                .extract()
                .response();
    }
}
