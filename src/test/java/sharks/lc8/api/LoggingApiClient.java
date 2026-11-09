package sharks.lc8.api;

import io.restassured.response.Response;
import sharks.lc8.models.UserDto;

import java.util.function.Supplier;

/**
 * Обёртка вокруг другого ApiClient: пишет строку лога до и после каждого запроса.
 *
 * Паттерн Decorator: обернуть объект, чтобы добавить новую возможность, не меняя сам объект.
 * Как чехол на телефон: телефон работает так же, чехол добавляет защиту.
 * Здесь "телефон" - настоящий клиент, "чехол" - логи.
 *
 * То же "меню" (ApiClient), что у настоящего клиента, поэтому код снаружи не замечает разницы.
 * Каждый метод: 1) пишет "начинаю запрос"; 2) передаёт работу клиенту внутри (delegate);
 * 3) пишет результат и возвращает ответ не тронутым.
 *
 * Почему не println прямо в SlotCityApiClient: клиент смешался бы с логами, и их нельзя было бы выключить.
 *   new LoggingApiClient(new SlotCityApiClient())   <- с логами
 *   new SlotCityApiClient()                         <- без логов
 * Сейчас используется только в DecoratorTest: в API-тестах запросы и ответы уже логируют фильтры ApiSpecs.
 */
public class LoggingApiClient implements ApiClient {

    // Клиент внутри обёртки, делает настоящую работу.
    // Тип ApiClient: обернуть можно любой клиент (в DecoratorTest - FakeApiClient).
    private final ApiClient delegate;

    public LoggingApiClient(ApiClient delegate) {
        this.delegate = delegate;
    }

    // Четыре метода только говорят, КАКОЙ запрос выполнить. Сами логи написаны один раз, в logged().
    // () -> ... = лямбда: кусок кода-параметр, выполнится позже, внутри logged().
    @Override
    public Response register(UserDto user) {
        return logged("register", () -> delegate.register(user));
    }

    @Override
    public Response login(UserDto user) {
        return logged("login", () -> delegate.login(user));
    }

    // delegate::guestSession - короткая запись лямбды () -> delegate.guestSession()
    @Override
    public Response guestSession() {
        return logged("guestSession", delegate::guestSession);
    }

    @Override
    public Response activatePromocode(String token, String promocode) {
        return logged("activatePromocode", () -> delegate.activatePromocode(token, promocode));
    }

    /**
     * Общий код логов, чтобы не копировать его четыре раза.
     * call - лямбда "выполни ЭТОТ запрос и дай Response" (Supplier = "поставщик").
     */
    private Response logged(String operation, Supplier<Response> call) {
        System.out.println("[API] ---> " + operation);
        long start = System.currentTimeMillis();

        try {
            // Здесь выполняется лямбда = настоящий запрос через delegate
            Response response = call.get();
            long duration = System.currentTimeMillis() - start;

            System.out.println("[API] <--- " + operation
                    + " | status=" + response.getStatusCode()
                    + " | time=" + duration + "ms"
                    + " | body=" + shorten(response.asString()));
            return response;
        } catch (RuntimeException e) {
            // Пишем ошибку в лог и бросаем дальше: обёртка не должна прятать проблему от того, кто её вызвал.
            long duration = System.currentTimeMillis() - start;
            System.out.println("[API] <--- " + operation
                    + " | FAILED after " + duration + "ms"
                    + " | " + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw e;
        }
    }

    // Огромный ответ залил бы консоль, поэтому печатаем только первые 200 символов.
    // условие ? A : B = короткий if-else
    private String shorten(String body) {
        int limit = 200;
        return body.length() <= limit ? body : body.substring(0, limit) + "...";
    }
}
