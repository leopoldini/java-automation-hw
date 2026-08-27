package sharks.lc6.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SlotCityApiClient {

    private static final String BASE_URL = "https://stage.slotcity.ua";

    private Map<String, Object> getDevicePayload() {
        Map<String, Object> device = new HashMap<>();
        device.put("platform", "WEB");
        device.put("device_id", "9b07a82a58d36c9435a40704a8381570");
        device.put("device_model", "Web Windows Chrome 151");
        device.put("browser_name", "Chrome");
        device.put("browser_version", "151.0.0.0");
        device.put("os_version", "10");
        device.put("user_agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        device.put("language", "uk");
        return device;
    }

    public Response register(String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("type", "email");
        body.put("email", email);
        body.put("password", password);
        body.put("promokey", "");
        body.put("is_accept", 1);
        body.put("device", getDevicePayload());

        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("localization", "ua")
                .log().all()
                .body(body)
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

    public Response login(String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("type", "email");
        body.put("email", email);
        body.put("password", password);
        body.put("device", getDevicePayload());

        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("localization", "ua")
                .log().all()
                .body(body)
                .when()
                .post("/auth/login?on_device=true")
                .then()
                .log().all()
                .extract().response();
    }

    public Response activatePromocode(String token, String promocode) {
        Map<String, Object> body = new HashMap<>();
        body.put("promocode", promocode);

        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("localization", "ua")
                .header("authorization", "Bearer " + token)
                .log().all()
                .body(body)
                .when()
                .post("/apiv2/promocodes/activate")
                .then()
                .log().all()
                .extract().response();
    }
}