package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.LoginRequestDto;
import sharks.lc8.models.PromocodeRequestDto;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;
import sharks.lc8.specs.ApiSpecs;

import static io.restassured.RestAssured.given;

public class SlotCityApiClient implements ApiClient {

    @Override
    public Response register(UserDto user) {
        return given()
                .spec(ApiSpecs.base())
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
                .spec(ApiSpecs.authorized(token))
                .body(PromocodeRequestDto.builder().code(promocode).build())
                .when()
                .post("/apiv2/promocodes/activate")
                .then()
                .extract()
                .response();
    }
}