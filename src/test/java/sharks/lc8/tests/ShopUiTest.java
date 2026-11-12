package sharks.lc8.tests;

import org.testng.annotations.Test;
import sharks.lc8.facade.ShopUiFacade;

/*
 UI-тесты. Браузер даёт BaseUiTest, шаги даёт фасад, поэтому здесь нет ни селекторов, ни кода браузера.
 Проверки внутри шагов фасада, поэтому тест - это только сценарий.
 */
public class ShopUiTest extends BaseUiTest {

    @Test
    public void shouldRegisterNewUser() {
        new ShopUiFacade(page)
                .openShop()
                .registerNewUser();
    }

    // Регистрация сама логинит пользователя, поэтому сначала logout:
    // иначе шаг логина ничего бы не доказал, пользователь и так внутри.
    @Test
    public void shouldLoginAfterLogout() {
        new ShopUiFacade(page)
                .openShop()
                .registerNewUser()
                .logout()
                .loginAsRegisteredUser();
    }

    @Test
    public void shouldRejectInvalidCredentials() {
        new ShopUiFacade(page)
                .openShop()
                .loginWithInvalidCredentials();
    }
}
