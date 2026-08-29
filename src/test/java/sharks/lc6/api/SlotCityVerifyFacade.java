package sharks.lc6.api;

import io.restassured.response.Response;
import org.testng.Assert;
import sharks.lc6.models.AuthResponse;
import sharks.lc6.models.GuestResponse;
import sharks.lc6.models.SessionData;

public class SlotCityVerifyFacade {

    public void verifyRegistrationSuccess(AuthResponse authResponse, SessionData session) {
        Assert.assertNotNull(authResponse, "Auth response should not be null");
        Assert.assertTrue(authResponse.isStatus(), "Registration status in POJO model should be true");
        Assert.assertNotNull(session.getToken(), "User token should be stored in session");
    }

    public void verifyLogoutSuccess(GuestResponse guestResponse, SessionData session) {
        Assert.assertNotNull(guestResponse, "Guest response should not be null");
        Assert.assertNotNull(guestResponse.getToken(), "Guest token should not be null");
        Assert.assertEquals(session.getToken(), guestResponse.getToken(), "Session token should match guest token");
    }

    public void verifyLoginSuccess(AuthResponse authResponse, SessionData session) {
        Assert.assertNotNull(authResponse, "Auth response should not be null");
        Assert.assertTrue(authResponse.isStatus(), "Login status in POJO model should be true");
        Assert.assertNotNull(session.getToken(), "New token should be stored in session after login");
    }

    public void verifyPromocodeResponse(Response response) {
        Assert.assertEquals(response.getStatusCode(), 200, "Promocode response status should be 200");
    }
}