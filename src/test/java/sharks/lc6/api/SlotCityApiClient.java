package sharks.lc6.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import sharks.lc6.models.LoginRequest;
import sharks.lc6.models.PromocodeRequest;
import sharks.lc6.models.RegisterRequest;
import sharks.lc6.models.UserData;

import static io.restassured.RestAssured.given;

public class SlotCityApiClient {

    private static final String BASE_URL = "https://stage.slotcity.ua";

    public Response register(UserData user) {
        RegisterRequest registerRequest = new RegisterRequest(user);

        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("localization", "ua")
                .log().all()
                .body(registerRequest)
                .when()
                .post("/auth/v2/register?on_device=true")
                .then()
                .log().all()
                .extract().response();
    }

    public Response logoutGuest() {
        return given()
                .baseUri(BASE_URL)
                .header("localization", "ua")
                .log().all()
                .when()
                .get("/auth/guest")
                .then()
                .log().all()
                .extract().response();
    }

    public Response login(UserData user) {
        LoginRequest loginRequest = new LoginRequest(user);

        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("localization", "ua")
                .log().all()
                .body(loginRequest)
                .when()
                .post("/auth/login?on_device=true")
                .then()
                .log().all()
                .extract().response();
    }

    public Response activatePromocode(String token, String promocode) {
        PromocodeRequest promoRequest = new PromocodeRequest(promocode);

        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("localization", "ua")
                .header("authorization", "Bearer " + token)
                .log().all()
                .body(promoRequest)
                .when()
                .post("/apiv2/promocodes/activate")
                .then()
                .log().all()
                .extract().response();
    }
}