package sharks.lc8.tests;

import org.testng.annotations.Test;
import sharks.lc8.facade.ShopUiFacade;

/*
UI tests. The browser comes from BaseUiTest, the steps from the facade - no selectors and no browser code here. The asserts live inside the facade steps, so a test is just the story.
 */
public class ShopUiTest extends BaseUiTest {

    @Test
    public void shouldRegisterNewUser() {
        new ShopUiFacade(page)
                .openShop()
                .registerNewUser();
    }

    // Registration already logs the user in, so logout comes first -
    // otherwise the login step would prove nothing.
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