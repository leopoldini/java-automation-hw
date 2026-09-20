package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

import java.util.function.Supplier;

/**
 * WHAT IT IS: a "wrapper" around another ApiClient that prints a log line before and after
 * every request.
 *
 * PATTERN: Decorator = "wrap an object to add a new ability, without changing the object itself".
 * Like putting a phone in a case: the phone works the same, the case just adds something
 * (protection). Here the "phone" is the real client and the "case" is the logging.
 *
 * HOW IT WORKS: this class has the SAME menu (ApiClient) as the real client, so the code that
 * uses it can't tell the difference. Each method does 3 things:
 *   1. prints "I'm starting this request";
 *   2. passes the work to the wrapped client (the "delegate");
 *   3. prints the result and returns the answer untouched.
 *
 * WHY NOT JUST PUT println INTO SlotCityApiClient: then the real client would be mixed with
 * logging code, and we couldn't switch logs off. With a wrapper, logs are optional:
 *   new LoggingApiClient(new SlotCityApiClient())   <- with logs
 *   new SlotCityApiClient()                         <- without logs
 *
 * SAY IT IN DEFENCE: "The decorator adds logging to any ApiClient without changing its code."
 */
public class LoggingApiClient implements ApiClient {

    // The client we wrap. It does the real work.
    private final ApiClient delegate;

    public LoggingApiClient(ApiClient delegate) {
        this.delegate = delegate;
    }

    // The four methods below only say WHICH request to run.
    // The logging itself is written once, in logged() at the bottom.
    @Override
    public Response register(UserDto user) {
        return logged("register", () -> delegate.register(user));
    }

    @Override
    public Response login(UserDto user) {
        return logged("login", () -> delegate.login(user));
    }

    @Override
    public Response guestSession() {
        return logged("guestSession", delegate::guestSession);
    }

    @Override
    public Response activatePromocode(String token, String promocode) {
        return logged("activatePromocode", () -> delegate.activatePromocode(token, promocode));
    }

    /**
     * The shared logging code, so we don't copy-paste it four times.
     * "call" is a piece of code passed in as a parameter (a lambda): "run THIS request and
     * give me its Response". That way this method doesn't care which request it is timing.
     */
    private Response logged(String operation, Supplier<Response> call) {
        System.out.println("[API] ---> " + operation);
        long start = System.currentTimeMillis();

        try {
            Response response = call.get();
            long duration = System.currentTimeMillis() - start;

            System.out.println("[API] <--- " + operation
                    + " | status=" + response.getStatusCode()
                    + " | time=" + duration + "ms"
                    + " | body=" + shorten(response.asString()));
            return response;
        } catch (RuntimeException e) {
            // Write the error to the log, then throw it again. A wrapper must never hide
            // a problem from the caller.
            long duration = System.currentTimeMillis() - start;
            System.out.println("[API] <--- " + operation
                    + " | FAILED after " + duration + "ms"
                    + " | " + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw e;
        }
    }

    // A huge answer would flood the console, so we print only the first 200 characters.
    private String shorten(String body) {
        int limit = 200;
        return body.length() <= limit ? body : body.substring(0, limit) + "...";
    }
}
