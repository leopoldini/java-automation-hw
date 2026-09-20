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
 Fluent: every step returns this, so steps chain with dots.
 Keeps state between steps (current user + token), so every test needs its own facade.
Asserts live inside the steps on purpose - a failure points straight at the broken step.
 */
public class AuthApiFacade {

    // Field type is the interface, not SlotCityApiClient - that is what lets the decorator in.
    private final ApiClient apiClient;
    private final ConfigManager config = ConfigManager.getInstance();

    private UserDto currentUser;
    private String token;

    public AuthApiFacade() {
        this(new SlotCityApiClient());
    }

    // Used by DecoratorTest: new AuthApiFacade(new LoggingApiClient(new SlotCityApiClient()))
    public AuthApiFacade(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    // @Step = named step in the Allure report
    @Step("Register new user")
    public AuthApiFacade registerNewUser() {
        // Builder. Email must be new every run - the server rejects an already registered one.
        currentUser = UserDto.builder()
                .email(generateEmail())
                .password(config.getDefaultPassword())
                .build();

        Response response = apiClient.register(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Registration status code");

        // response.as() = Jackson turns JSON into an object, no jsonPath strings
        AuthResponseDto auth = response.as(AuthResponseDto.class);
        // This API answers 200 even on failure, so the body has to be checked too
        Assert.assertTrue(auth.isStatus(), "Registration failed: " + response.asString());

        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after registration");
        return this;
    }

    // There is no logout endpoint: /auth/guest issues a guest token that replaces the user one.
    @Step("Switch to guest session")
    public AuthApiFacade switchToGuest() {
        Response response = apiClient.guestSession();
        Assert.assertEquals(response.getStatusCode(), 200, "Guest session status code");

        // Flat JSON here - {"token": "..."}, no "user" object, hence a separate DTO
        GuestResponseDto guest = response.as(GuestResponseDto.class);
        String guestToken = guest.getToken();

        Assert.assertNotNull(guestToken, "Guest token");
        // Proves we really switched: a guest token cannot be the same as the user one
        Assert.assertNotEquals(guestToken, token, "Guest token must differ from user token");

        token = guestToken;
        return this;
    }

    @Step("Login with current user")
    public AuthApiFacade loginAsCurrentUser() {
        // Fails with a clear message instead of an NPE if the steps are chained in a wrong order
        Assert.assertNotNull(currentUser, "User must be registered before login");

        Response response = apiClient.login(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Login status code");

        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertTrue(auth.isStatus(), "Login failed: " + response.asString());

        // Login returns a fresh token, it replaces the previous (or guest) one
        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after login");
        return this;
    }

    // Overload: no argument = take the code from config.properties
    @Step("Activate promocode")
    public AuthApiFacade activatePromocode() {
        return activatePromocode(config.getPromocode());
    }

    @Step("Activate promocode {promocode}")
    public AuthApiFacade activatePromocode(String promocode) {
        Response response = apiClient.activatePromocode(token, promocode);
        Assert.assertEquals(response.getStatusCode(), 200, "Promocode activation status code");

        PromocodeResponseDto result = response.as(PromocodeResponseDto.class);
        // "reason" carries the server explanation, e.g. "Promocode not found"
        Assert.assertTrue(result.isStatus(), "Promocode activation failed: " + result.getReason());
        return this;
    }

    // currentTimeMillis instead of Random: random numbers started colliding with used emails
    private String generateEmail() {
        long suffix = 1000 + System.currentTimeMillis() % 1_000_000_000L;
        return "leopold+" + suffix + "@sharkscode.com";
    }
}