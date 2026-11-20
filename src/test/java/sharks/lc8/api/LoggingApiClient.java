package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

import java.util.function.Supplier;

/**
 * A wrapper around another ApiClient: writes a log line before and after every request.
 *
 * Decorator pattern: wrap an object to add a new feature without changing the object itself.
 * Like a phone case: the phone works the same, the case adds protection.
 * Here the "phone" is the real client and the "case" is the logs.
 *
 * Same "menu" (ApiClient) as the real client, so the code outside does not notice the difference.
 * Every method: 1) writes "starting the request"; 2) passes the work to the client inside (delegate);
 * 3) writes the result and returns the response untouched.
 *
 * Why not println right inside SlotCityApiClient: the client would be mixed with logs, and logs could not be switched off.
 *   new LoggingApiClient(new SlotCityApiClient())   <- with logs
 *   new SlotCityApiClient()                         <- without logs
 * Used only in DecoratorTest for now: in the API tests the ApiSpecs filters already log requests and responses.
 */
public class LoggingApiClient implements ApiClient {

    // The client inside the wrapper, it does the real work.
    // Type ApiClient: any client can be wrapped (in DecoratorTest it is FakeApiClient).
    private final ApiClient delegate;

    public LoggingApiClient(ApiClient delegate) {
        this.delegate = delegate;
    }

    // The four methods only say WHICH request to run. The logging itself is written once, in logged().
    // () -> ... = lambda: a piece of code passed as a parameter, it runs later, inside logged().
    @Override
    public Response register(UserDto user) {
        return logged("register", () -> delegate.register(user));
    }

    @Override
    public Response login(UserDto user) {
        return logged("login", () -> delegate.login(user));
    }

    // delegate::guestSession - a short form of the lambda () -> delegate.guestSession()
    @Override
    public Response guestSession() {
        return logged("guestSession", delegate::guestSession);
    }

    @Override
    public Response activatePromocode(String token, String promocode) {
        return logged("activatePromocode", () -> delegate.activatePromocode(token, promocode));
    }

    /**
     * Shared logging code, so it is not copied four times.
     * call - a lambda "run THIS request and give me the Response" (Supplier = "provider").
     */
    private Response logged(String operation, Supplier<Response> call) {
        System.out.println("[API] ---> " + operation);
        long start = System.currentTimeMillis();

        try {
            // The lambda runs here = the real request through delegate
            Response response = call.get();
            long duration = System.currentTimeMillis() - start;

            System.out.println("[API] <--- " + operation
                    + " | status=" + response.getStatusCode()
                    + " | time=" + duration + "ms"
                    + " | body=" + shorten(response.asString()));
            return response;
        } catch (RuntimeException e) {
            // Log the error and throw it further: the wrapper must not hide the problem from the caller.
            long duration = System.currentTimeMillis() - start;
            System.out.println("[API] <--- " + operation
                    + " | FAILED after " + duration + "ms"
                    + " | " + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw e;
        }
    }

    // A huge response would flood the console, so we print only the first 200 characters.
    // condition ? A : B = a short if-else
    private String shorten(String body) {
        int limit = 200;
        return body.length() <= limit ? body : body.substring(0, limit) + "...";
    }
}
