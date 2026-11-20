package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.LoginRequestDto;
import sharks.lc8.models.PromocodeRequestDto;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;
import sharks.lc8.specs.ApiSpecs;

import static io.restassured.RestAssured.given;

/**
 * The real API client: sends HTTP requests to the SlotCity server with Rest Assured.
 * The only class that knows the paths (/auth/login and so on): if a path changes, I fix it only here.
 *
 * Every method reads like a sentence:
 *   given()  = "given": shared settings (spec) + request body (body)
 *   when()   = "when": HTTP method and path (post / get)
 *   then().extract().response() = "then": just take the response, the checks are in the facade
 */
public class SlotCityApiClient implements ApiClient {

    // @Override: a method from the ApiClient "menu" - same name and parameters, but its own body (the real request).
    // If I make a mistake in the name or parameters, IDEA shows an error.
    @Override
    public Response register(UserDto user) {
        return given()
                .spec(ApiSpecs.base())
                // We pass a plain Java object, Rest Assured turns it into JSON by itself (serialization).
                // from() converts UserDto into the server format.
                .body(RegisterRequestDto.from(user))
                .when()
                // Only the path: the server address is already in ApiSpecs.base()
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

    // GET and no body: nothing to send, we only get a guest token
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
                // authorized = the usual settings + the token in a header: the server knows WHO activates the promocode
                .spec(ApiSpecs.authorized(token))
                .body(PromocodeRequestDto.builder().code(promocode).build())
                .when()
                .post("/apiv2/promocodes/activate")
                .then()
                .extract()
                .response();
    }
}
