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
 * Тесты декоратора без сервера: внутрь обёртки кладём FakeApiClient, поэтому проверяется только работа
 * самой обёртки - передать вызов, записать лог, сократить длинное тело, не спрятать ошибку.
 * Без интернета - безопасно запускать вживую.
 * Обернуть фальшивый клиент можно только потому, что оба реализуют ApiClient - это и есть паттерн.
 */
public class DecoratorTest {

    private static final UserDto USER = UserDto.builder()
            .email("decorator@test.com")
            .password("Test12345")
            .build();

    private PrintStream originalOut;
    private ByteArrayOutputStream console;

    // Обёртка пишет логи в System.out, поэтому перед каждым тестом перенаправляем консоль
    // в буфер, который тест может прочитать
    @BeforeMethod
    public void captureConsole() {
        originalOut = System.out;
        console = new ByteArrayOutputStream();
        System.setOut(new PrintStream(console, true, StandardCharsets.UTF_8));
    }

    // alwaysRun: вернуть консоль даже после упавшего теста, иначе следующие тесты "замолчат"
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

        // assertSame, а не assertEquals: это тот же самый объект, обёртка ничего не поменяла
        Assert.assertSame(actual, expected, "Decorator must return the wrapped client's response");
        // И вызов действительно дошёл до клиента внутри, ровно один раз
        Assert.assertEquals(fake.calls, List.of("login"));
    }

    @Test
    public void shouldLogRequestAndResponse() {
        ApiClient decorated = new LoggingApiClient(new FakeApiClient(response(200, "{\"status\":true}")));

        decorated.register(USER);

        // Должны быть обе стрелки: часть обёртки "до" запроса и часть "после"
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

        // try/catch, а не expectedExceptions: после исключения нужно ещё проверить лог
        try {
            decorated.login(USER);
            // Дошли до этой строки = обёртка спрятала ошибку = баг
            Assert.fail("Exception from the wrapped client must not be swallowed");
        } catch (IllegalStateException expected) {
            Assert.assertEquals(expected.getMessage(), "server is down");
        }

        // Сначала записали в лог, потом бросили дальше: упавший вызов не должен пропасть из лога
        String log = console.toString(StandardCharsets.UTF_8);
        Assert.assertTrue(log.contains("FAILED"), log);
        Assert.assertTrue(log.contains("server is down"), log);
    }

    // Готовый ответ без отправки по сети
    private static Response response(int statusCode, String body) {
        return new ResponseBuilder()
                .setStatusCode(statusCode)
                .setBody(body)
                .build();
    }

    /**
     * Фальшивый клиент вместо настоящего: возвращает заготовленный ответ (или бросает заготовленную
     * ошибку) и записывает, какие методы вызывали. Без сети и без библиотек вроде Mockito.
     * Два конструктора с разными параметрами - перегрузка.
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

        // Для всех методов одинаково: записать вызов, потом бросить ошибку или вернуть ответ
        private Response answer(String method) {
            calls.add(method);
            if (failure != null) {
                throw failure;
            }
            return response;
        }
    }
}