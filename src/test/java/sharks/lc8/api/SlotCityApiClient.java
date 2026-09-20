package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.LoginRequestDto;
import sharks.lc8.models.PromocodeRequestDto;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;
import sharks.lc8.specs.ApiSpecs;

import static io.restassured.RestAssured.given;

/**
 * WHAT IT IS: the REAL API client. It really sends HTTP requests to the SlotCity server,
 * using the RestAssured library.
 *
 * WHY: this is the only class that knows the endpoint addresses (/auth/login etc.).
 * If an address changes, we fix it here and nowhere else.
 *
 * EVERY METHOD LOOKS THE SAME, read it like a sentence:
 *   given()   = "Given a prepared request" (common settings + body)
 *   when()    = "When I send it" (which method and which address)
 *   then().extract().response() = "Then give me the answer" (we only take it, we don't check it here)
 */
public class SlotCityApiClient implements ApiClient {

    @Override
    public Response register(UserDto user) {
        return given()
                .spec(ApiSpecs.base())
                // We hand over a normal Java object and RestAssured turns it into JSON by itself.
                // RegisterRequestDto.from(...) converts our simple UserDto into the format the server wants.
                .body(RegisterRequestDto.from(user))
                .when()
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
                // "authorized" = the usual settings + the token in the header,
                // so the server knows WHICH user is asking.
                .spec(ApiSpecs.authorized(token))
                .body(PromocodeRequestDto.builder().code(promocode).build())
                .when()
                .post("/apiv2/promocodes/activate")
                .then()
                .extract()
                .response();
    }
}
