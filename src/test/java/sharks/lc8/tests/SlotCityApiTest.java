package sharks.lc8.tests;

import org.testng.annotations.Test;

/*
 API-тесты. extends BaseApiTest: наследуем поле auth и новый фасад перед каждым тестом.
 Проверки живут внутри шагов фасада, поэтому тест - это просто цепочка бизнес-шагов.
 Четыре отдельных теста вместо одного длинного: каждый проверяет одну вещь, и по упавшему сразу видно, что сломалось.
 */
public class SlotCityApiTest extends BaseApiTest {

    @Test
    public void shouldRegisterNewUser() {
        auth.registerNewUser();
    }

    // switchToGuest сам проверяет, что гостевой токен отличается от токена пользователя
    @Test
    public void shouldSwitchToGuestSession() {
        auth.registerNewUser()
                .switchToGuest();
    }

    // Аккаунт должен пережить гостевую сессию: повторный логин это доказывает
    @Test
    public void shouldLoginAfterGuestSession() {
        auth.registerNewUser()
                .switchToGuest()
                .loginAsCurrentUser();
    }

    // Промокоду нужен токен, поэтому регистрация -> логин -> активация.
    // Сейчас падает: промокод TESTKKTT2 на стенде истёк (status false, reason "expired promocode").
    @Test
    public void shouldActivatePromocode() {
        auth.registerNewUser()
                .loginAsCurrentUser()
                .activatePromocode();
    }
}