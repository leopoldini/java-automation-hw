package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

/**
 * WHAT IT IS: a list of the requests our tests can send to the SlotCity server.
 * It is an interface = only a "menu" (what we can do), with no code inside.
 * The real code is in SlotCityApiClient.
 *
 * WHY A MENU AND NOT JUST THE CLASS: the rest of the framework only knows the menu, not the
 * real class behind it. So we can swap what is behind it without touching anything else:
 *  - wrap it in LoggingApiClient to get logs (Decorator pattern);
 *  - use a fake one in a unit test, so we don't need the real server.
 * (Like a power socket: it doesn't care if you plug in a lamp or a kettle.)
 *
 * Each method just sends a request and returns the raw answer (Response). It checks NOTHING.
 * Checking the answer is the job of the facade (AuthApiFacade).
 */
public interface ApiClient {

    Response register(UserDto user);

    Response login(UserDto user);

    Response guestSession();

    Response activatePromocode(String token, String promocode);
}
