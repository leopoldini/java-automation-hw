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
 Facade: a simple entrance in front of a complex system. The test calls clear business steps
 (registerNewUser, switchToGuest, loginAsCurrentUser, activatePromocode),
 and the Builder, client, DTOs, token and checks are hidden inside.
 Fluent: every step returns this, so the steps go in a chain, dot after dot.
 Keeps state between steps (user + token), so every test needs its own facade (BaseApiTest).
 Checks are inside the steps: if something breaks, the message and the Allure step show where right away.
 */
public class AuthApiFacade {

    // The field type is the ApiClient interface. To turn logs on, it is enough to change this line
    // to new LoggingApiClient(new SlotCityApiClient()).
    private final ApiClient apiClient = new SlotCityApiClient();
    private final ConfigManager config = ConfigManager.getInstance();

    // State between steps: the current user and their token
    private UserDto currentUser;
    private String token;

    // @Step = the step name in the Allure report
    @Step("Register new user")
    public AuthApiFacade registerNewUser() {
        // Builder. The email must be new every time: the server does not accept an already registered one.
        currentUser = UserDto.builder()
                .email(generateEmail())
                .password(config.getDefaultPassword())
                .build();

        Response response = apiClient.register(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Registration status code");

        // response.as() = Jackson turns JSON into an object (deserialization)
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        // The API answers 200 even on failure, so we also check status in the response body
        Assert.assertTrue(auth.isStatus(), "Registration failed: " + response.asString());

        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after registration");
        return this;
    }

    // The API has no logout: /auth/guest gives a guest token, and it replaces the user token.
    @Step("Switch to guest session")
    public AuthApiFacade switchToGuest() {
        Response response = apiClient.guestSession();
        Assert.assertEquals(response.getStatusCode(), 200, "Guest session status code");

        // The response is flat - {"token": "..."}, without a "user" object, so it has its own DTO
        GuestResponseDto guest = response.as(GuestResponseDto.class);
        String guestToken = guest.getToken();

        Assert.assertNotNull(guestToken, "Guest token");
        // Proves that we really switched: the guest token cannot be the same as the user token
        Assert.assertNotEquals(guestToken, token, "Guest token must differ from user token");

        token = guestToken;
        return this;
    }

    @Step("Login with current user")
    public AuthApiFacade loginAsCurrentUser() {
        // Login before registration (steps in the wrong order) -> a clear error right away, not a NullPointerException
        Assert.assertNotNull(currentUser, "User must be registered before login");

        Response response = apiClient.login(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Login status code");

        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertTrue(auth.isStatus(), "Login failed: " + response.asString());

        // Login gives a new token, it replaces the previous one (or the guest one)
        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after login");
        return this;
    }

    // Overloading: without a parameter = the code from config.properties, with a parameter = any code
    @Step("Activate promocode")
    public AuthApiFacade activatePromocode() {
        return activatePromocode(config.getPromocode());
    }

    @Step("Activate promocode {promocode}")
    public AuthApiFacade activatePromocode(String promocode) {
        Response response = apiClient.activatePromocode(token, promocode);
        Assert.assertEquals(response.getStatusCode(), 200, "Promocode activation status code");

        PromocodeResponseDto result = response.as(PromocodeResponseDto.class);
        // reason - the server's explanation, for example "expired promocode"
        Assert.assertTrue(result.isStatus(), "Promocode activation failed: " + result.getReason());
        return this;
    }

    // currentTimeMillis, not Random: random numbers started to match emails that were already taken
    private String generateEmail() {
        long suffix = 1000 + System.currentTimeMillis() % 1_000_000_000L;
        return "leopold+" + suffix + "@sharkscode.com";
    }
}