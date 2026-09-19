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

public class AuthApiFacade {

    private final ApiClient apiClient;
    private final ConfigManager config = ConfigManager.getInstance();

    private UserDto currentUser;
    private String token;

    public AuthApiFacade() {
        this(new SlotCityApiClient());
    }

    public AuthApiFacade(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Step("Register new user")
    public AuthApiFacade registerNewUser() {
        currentUser = UserDto.builder()
                .email(generateEmail())
                .password(config.getDefaultPassword())
                .build();

        Response response = apiClient.register(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Registration status code");

        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertTrue(auth.isStatus(), "Registration failed: " + response.asString());

        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after registration");
        return this;
    }

    @Step("Switch to guest session")
    public AuthApiFacade switchToGuest() {
        Response response = apiClient.guestSession();
        Assert.assertEquals(response.getStatusCode(), 200, "Guest session status code");

        GuestResponseDto guest = response.as(GuestResponseDto.class);
        String guestToken = guest.getToken();

        Assert.assertNotNull(guestToken, "Guest token");
        Assert.assertNotEquals(guestToken, token, "Guest token must differ from user token");

        token = guestToken;
        return this;
    }

    @Step("Login with current user")
    public AuthApiFacade loginAsCurrentUser() {
        Assert.assertNotNull(currentUser, "User must be registered before login");

        Response response = apiClient.login(currentUser);
        Assert.assertEquals(response.getStatusCode(), 200, "Login status code");

        AuthResponseDto auth = response.as(AuthResponseDto.class);
        Assert.assertTrue(auth.isStatus(), "Login failed: " + response.asString());

        token = auth.getUser().getToken();
        Assert.assertNotNull(token, "Token after login");
        return this;
    }

    @Step("Activate promocode")
    public AuthApiFacade activatePromocode() {
        return activatePromocode(config.getPromocode());
    }

    @Step("Activate promocode {promocode}")
    public AuthApiFacade activatePromocode(String promocode) {
        Response response = apiClient.activatePromocode(token, promocode);
        Assert.assertEquals(response.getStatusCode(), 200, "Promocode activation status code");

        PromocodeResponseDto result = response.as(PromocodeResponseDto.class);
        Assert.assertTrue(result.isStatus(), "Promocode activation failed: " + result.getReason());
        return this;
    }

    @Step("Try to activate unknown promocode {promocode}")
    public AuthApiFacade activateUnknownPromocode(String promocode) {
        Response response = apiClient.activatePromocode(token, promocode);
        Assert.assertEquals(response.getStatusCode(), 200, "Promocode activation status code");

        PromocodeResponseDto result = response.as(PromocodeResponseDto.class);
        Assert.assertFalse(result.isStatus(), "Unknown promocode should not be activated");
        Assert.assertEquals(result.getReason(), "Promocode not found", "Rejection reason");
        return this;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public String getToken() {
        return token;
    }

    private String generateEmail() {
        long suffix = 1000 + System.currentTimeMillis() % 1_000_000_000L;
        return "leopold+" + suffix + "@sharkscode.com";
    }
}