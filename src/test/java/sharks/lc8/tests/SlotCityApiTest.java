package sharks.lc8.tests;

import org.testng.annotations.Test;
import sharks.lc8.facade.AuthApiFacade;

public class SlotCityApiTest {

    @Test
    public void shouldRegisterNewUser() {
        new AuthApiFacade()
                .registerNewUser();
    }

    @Test
    public void shouldSwitchToGuestSession() {
        new AuthApiFacade()
                .registerNewUser()
                .switchToGuest();
    }

    @Test
    public void shouldLoginAfterGuestSession() {
        new AuthApiFacade()
                .registerNewUser()
                .switchToGuest()
                .loginAsCurrentUser();
    }

    @Test
    public void shouldActivatePromocode() {
        new AuthApiFacade()
                .registerNewUser()
                .loginAsCurrentUser()
                .activatePromocode();
    }
}