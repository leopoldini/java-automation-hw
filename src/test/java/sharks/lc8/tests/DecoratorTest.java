package sharks.lc8.tests;

import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sharks.lc8.api.ApiClient;
import sharks.lc8.api.LoggingApiClient;
import sharks.lc8.models.UserDto;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator tests without a server: we put FakeApiClient inside the wrapper, so only the work of
 * the wrapper itself is checked - pass the call, write the log, shorten a long body, do not hide an error.
 * No internet - safe to run live.
 * We can wrap a fake client only because both implement ApiClient - that is the pattern itself.
 */
public class DecoratorTest {

    private static final UserDto USER = UserDto.builder()
            .email("decorator@test.com")
            .password("Test12345")
            .build();

    private PrintStream originalOut;
    private ByteArrayOutputStream console;

    // The wrapper writes logs to System.out, so before every test we redirect the console
    // into a buffer the test can read
    @BeforeMethod
    public void captureConsole() {
        originalOut = System.out;
        console = new ByteArrayOutputStream();
        System.setOut(new PrintStream(console, true, StandardCharsets.UTF_8));
    }

    // alwaysRun: give the console back even after a failed test, otherwise the next tests "go silent"
    @AfterMethod(alwaysRun = true)
    public void restoreConsole() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldDelegateToWrappedClientAndReturnItsResponse() {
        Response expected = response(200, "{\"status\":true}");
        FakeApiClient fake = new FakeApiClient(expected);
        ApiClient decorated = new LoggingApiClient(fake);

        Response actual = decorated.login(USER);

        // assertSame, not assertEquals: it is the very same object, the wrapper changed nothing
        Assert.assertSame(actual, expected, "Decorator must return the wrapped client's response");
        // And the call really reached the client inside, exactly once
        Assert.assertEquals(fake.calls, List.of("login"));
    }

    @Test
    public void shouldLogRequestAndResponse() {
        ApiClient decorated = new LoggingApiClient(new FakeApiClient(response(200, "{\"status\":true}")));

        decorated.register(USER);

        // Both arrows must be there: the "before" part of the wrapper and the "after" part
        String log = console.toString(StandardCharsets.UTF_8);
        Assert.assertTrue(log.contains("[API] ---> register"), log);
        Assert.assertTrue(log.contains("[API] <--- register"), log);
        Assert.assertTrue(log.contains("status=200"), log);
        Assert.assertTrue(log.contains("{\"status\":true}"), log);
    }

    @Test
    public void shouldShortenLongResponseBody() {
        String longBody = "x".repeat(500);
        ApiClient decorated = new LoggingApiClient(new FakeApiClient(response(200, longBody)));

        decorated.guestSession();

        String log = console.toString(StandardCharsets.UTF_8);
        Assert.assertFalse(log.contains(longBody), "Full body must not be printed");
        Assert.assertTrue(log.contains("..."), "Shortened body must end with ...");
    }

    @Test
    public void shouldLogFailureAndRethrowException() {
        FakeApiClient fake = new FakeApiClient(new IllegalStateException("server is down"));
        ApiClient decorated = new LoggingApiClient(fake);

        // try/catch, not expectedExceptions: after the exception we still need to check the log
        try {
            decorated.login(USER);
            // Reaching this line = the wrapper hid the error = a bug
            Assert.fail("Exception from the wrapped client must not be swallowed");
        } catch (IllegalStateException expected) {
            Assert.assertEquals(expected.getMessage(), "server is down");
        }

        // First written to the log, then thrown further: a failed call must not disappear from the log
        String log = console.toString(StandardCharsets.UTF_8);
        Assert.assertTrue(log.contains("FAILED"), log);
        Assert.assertTrue(log.contains("server is down"), log);
    }

    // A ready response without sending anything over the network
    private static Response response(int statusCode, String body) {
        return new ResponseBuilder()
                .setStatusCode(statusCode)
                .setBody(body)
                .build();
    }

    /**
     * A fake client instead of the real one: returns a ready response (or throws a ready error)
     * and records which methods were called. No network and no libraries like Mockito.
     * Two constructors with different parameters - overloading.
     */
    private static class FakeApiClient implements ApiClient {

        private final Response response;
        private final RuntimeException failure;
        private final List<String> calls = new ArrayList<>();

        FakeApiClient(Response response) {
            this.response = response;
            this.failure = null;
        }

        FakeApiClient(RuntimeException failure) {
            this.response = null;
            this.failure = failure;
        }

        @Override
        public Response register(UserDto user) {
            return answer("register");
        }

        @Override
        public Response login(UserDto user) {
            return answer("login");
        }

        @Override
        public Response guestSession() {
            return answer("guestSession");
        }

        @Override
        public Response activatePromocode(String token, String promocode) {
            return answer("activatePromocode");
        }

        // The same for all methods: record the call, then throw the error or return the response
        private Response answer(String method) {
            calls.add(method);
            if (failure != null) {
                throw failure;
            }
            return response;
        }
    }
}