package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

public interface ApiClient {

    Response register(UserDto user);

    Response login(UserDto user);

    Response guestSession();

    Response activatePromocode(String token, String promocode);
}