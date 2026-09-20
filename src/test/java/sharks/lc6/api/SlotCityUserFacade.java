package sharks.lc6.api;

import io.restassured.response.Response;
import sharks.lc6.models.AuthResponse;
import sharks.lc6.models.GuestResponse;
import sharks.lc6.models.SessionData;
import sharks.lc6.models.UserData;

public class SlotCityUserFacade {

    private final SlotCityApiClient apiClient = new SlotCityApiClient();

    public AuthResponse registerUser(UserData user, SessionData session) {
        Response response = apiClient.register(user);
        requireStatusOk(response, "Register");

        AuthResponse authResponse = response.as(AuthResponse.class);
        if (authResponse.isStatus() && authResponse.getUser() != null) {
            session.setToken(authResponse.getUser().getToken());
        }
        return authResponse;
    }

    public GuestResponse logoutUser(SessionData session) {
        Response response = apiClient.logoutGuest();
        requireStatusOk(response, "Logout (guest session)");

        GuestResponse guestResponse = response.as(GuestResponse.class);
        session.setToken(guestResponse.getToken());
        return guestResponse;
    }

    public AuthResponse loginUser(UserData user, SessionData session) {
        Response response = apiClient.login(user);
        requireStatusOk(response, "Login");

        AuthResponse authResponse = response.as(AuthResponse.class);
        if (authResponse.isStatus() && authResponse.getUser() != null) {
            session.setToken(authResponse.getUser().getToken());
        }
        return authResponse;
    }

    public Response activatePromocode(SessionData session, String promocode) {
        return apiClient.activatePromocode(session.getToken(), promocode);
    }

    // Instead of returning null on a bad answer (the caller would hit a NullPointerException
    // somewhere later and nobody would know why), we stop right here with a clear message:
    // which action failed, which status we got and what the server said.
    private void requireStatusOk(Response response, String action) {
        if (response.getStatusCode() != 200) {
            throw new IllegalStateException(action + " failed: expected HTTP 200 but got "
                    + response.getStatusCode() + ". Response body: " + response.asString());
        }
    }
}
