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
        if (response.getStatusCode() == 200) {
            AuthResponse authResponse = response.as(AuthResponse.class);
            if (authResponse.isStatus() && authResponse.getUser() != null) {
                session.setToken(authResponse.getUser().getToken());
            }
            return authResponse;
        }
        return null;
    }

    public GuestResponse logoutUser(SessionData session) {
        Response response = apiClient.logoutGuest();
        if (response.getStatusCode() == 200) {
            GuestResponse guestResponse = response.as(GuestResponse.class);
            session.setToken(guestResponse.getToken());
            return guestResponse;
        }
        return null;
    }

    public AuthResponse loginUser(UserData user, SessionData session) {
        Response response = apiClient.login(user);
        if (response.getStatusCode() == 200) {
            AuthResponse authResponse = response.as(AuthResponse.class);
            if (authResponse.isStatus() && authResponse.getUser() != null) {
                session.setToken(authResponse.getUser().getToken());
            }
            return authResponse;
        }
        return null;
    }

    public Response activatePromocode(SessionData session, String promocode) {
        return apiClient.activatePromocode(session.getToken(), promocode);
    }
}