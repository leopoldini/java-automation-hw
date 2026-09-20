package sharks.lc8.tests;

import org.testng.annotations.Test;

/*
 API tests. BaseApiTest hands each test a fresh auth facade, the asserts live inside the facade steps - so a test is just the chain of business steps.
 Four separate tests instead of one long end-to-end: each one checks a single thing, and a failure points straight at what broke.
 */
public class SlotCityApiTest extends BaseApiTest {

    @Test
    public void shouldRegisterNewUser() {
        auth.registerNewUser();
    }

    // switchToGuest itself asserts that the guest token differs from the user one
    @Test
    public void shouldSwitchToGuestSession() {
        auth.registerNewUser()
                .switchToGuest();
    }

    // The account must survive the guest session - logging back in proves it
    @Test
    public void shouldLoginAfterGuestSession() {
        auth.registerNewUser()
                .switchToGuest()
                .loginAsCurrentUser();
    }

    // A promocode needs a token, hence register -> log in -> activate
    @Test
    public void shouldActivatePromocode() {
        auth.registerNewUser()
                .loginAsCurrentUser()
                .activatePromocode();
    }
}