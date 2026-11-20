package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

/**
 * Interface = a "menu" of requests to the SlotCity server: WHAT can be done, with no code inside.
 * The code lives in the classes that implement it (implements):
 *  - SlotCityApiClient - real HTTP requests;
 *  - LoggingApiClient - a wrapper that adds logs (Decorator);
 *  - FakeApiClient in DecoratorTest - a ready-made answer, no server.
 * The rest of the code knows only the menu, so the implementation can be swapped without changing anything else.
 * (Like a wall socket: it does not care what you plug in - a lamp or a kettle.)
 *
 * The methods only send a request and return the raw response (Response), they check nothing.
 * All checks are in the facade AuthApiFacade.
 */
public interface ApiClient {

    Response register(UserDto user);

    Response login(UserDto user);

    Response guestSession();

    Response activatePromocode(String token, String promocode);
}
