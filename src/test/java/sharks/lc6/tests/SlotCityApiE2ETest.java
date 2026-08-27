package sharks.lc6.tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc5.utils.LoggerUtil;
import sharks.lc6.api.SlotCityApiClient;
import sharks.lc6.models.SessionData;

import java.util.Random;

public class SlotCityApiE2ETest {

    private final SlotCityApiClient apiClient = new SlotCityApiClient();
    private final SessionData session = SessionData.getInstance();
    private final String defaultPassword = "Test12345";
    private final String testPromocode = "D33A922F";

    private String generateEmail() {
        int randomNumber = 100 + new Random().nextInt(900);
        return "leopold+" + randomNumber + "@sharkscode.com";
    }

    @Test
    public void testUserRegistrationLogoutLoginAndPromocodeViaApi() {
        // STEP 1: Register User
        LoggerUtil.info("--- STEP 1: API Register User ---");
        String email = generateEmail();
        Response regResponse = apiClient.register(email, defaultPassword);

        Assert.assertEquals(regResponse.getStatusCode(), 200, "Registration status should be 200");
        Assert.assertTrue(regResponse.jsonPath().getBoolean("status"), "Registration response status should be true");

        String token = regResponse.jsonPath().getString("user.token");
        Assert.assertNotNull(token, "User token should not be null after registration");

        // STEP 2: Save User Data to Session Data
        LoggerUtil.info("--- STEP 2: Save User Data to Session Data ---");
        session.setEmail(email);
        session.setPassword(defaultPassword);
        session.setToken(token);

        Assert.assertEquals(session.getEmail(), email);
        Assert.assertNotNull(session.getToken());

        // STEP 3: Log Out (Guest mode)
        LoggerUtil.info("--- STEP 3: API Log Out (Guest Mode) ---");
        Response logoutResponse = apiClient.logoutGuest();
        Assert.assertEquals(logoutResponse.getStatusCode(), 200, "Guest logout status should be 200");

        session.setToken(null);

        // STEP 4: Login Created User
        LoggerUtil.info("--- STEP 4: API Login Created User ---");
        Response loginResponse = apiClient.login(session.getEmail(), session.getPassword());

        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login status should be 200");
        Assert.assertTrue(loginResponse.jsonPath().getBoolean("status"), "Login response status should be true");

        String newToken = loginResponse.jsonPath().getString("user.token");
        Assert.assertNotNull(newToken, "User token should not be null after login");
        session.setToken(newToken);

        // STEP 5: Activate Promocode
        LoggerUtil.info("--- STEP 5: API Activate Promocode ---");
        Response promoResponse = apiClient.activatePromocode(session.getToken(), testPromocode);
        Assert.assertEquals(promoResponse.getStatusCode(), 200, "Promocode activation HTTP status should be 200");
    }
}