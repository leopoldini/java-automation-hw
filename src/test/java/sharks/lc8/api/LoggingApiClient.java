package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

public class LoggingApiClient implements ApiClient {

    private final ApiClient delegate;

    public LoggingApiClient(ApiClient delegate) {
        this.delegate = delegate;
    }

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
        return logged("guestSession", () -> delegate.guestSession());
    }

    @Override
    public Response activatePromocode(String token, String promocode) {
        return logged("activatePromocode", () -> delegate.activatePromocode(token, promocode));
    }

    private Response logged(String operation, ApiCall call) {
        System.out.println("[API] ---> " + operation);
        long start = System.currentTimeMillis();

        try {
            Response response = call.execute();
            long duration = System.currentTimeMillis() - start;

            System.out.println("[API] <--- " + operation
                    + " | status=" + response.getStatusCode()
                    + " | time=" + duration + "ms"
                    + " | body=" + shorten(response.asString()));
            return response;
        } catch (RuntimeException e) {
            long duration = System.currentTimeMillis() - start;
            System.out.println("[API] <--- " + operation
                    + " | FAILED after " + duration + "ms"
                    + " | " + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw e;
        }
    }

    private String shorten(String body) {
        int limit = 200;
        return body.length() <= limit ? body : body.substring(0, limit) + "...";
    }

    @FunctionalInterface
    private interface ApiCall {
        Response execute();
    }
}