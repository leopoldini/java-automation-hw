package sharks.lc8.tests;

import org.testng.annotations.Test;

/*
 API tests. extends BaseApiTest: we inherit the auth field and a new facade before every test.
 The checks live inside the facade steps, so a test is just a chain of business steps.
 Four separate tests instead of one long one: each checks one thing, and a failed one shows right away what broke.
 */
public class SlotCityApiTest extends BaseApiTest {

    @Test
    public void shouldRegisterNewUser() {
        auth.registerNewUser();
    }

    // switchToGuest itself checks that the guest token differs from the user token
    @Test
    public void shouldSwitchToGuestSession() {
        auth.registerNewUser()
                .switchToGuest();
    }

    // The account must survive the guest session: logging in again proves it
    @Test
    public void shouldLoginAfterGuestSession() {
        auth.registerNewUser()
                .switchToGuest()
                .loginAsCurrentUser();
    }

    // The promocode needs a token, so register -> login -> activate.
    // Fails now: the promocode TESTKKTT2 has expired on stage (status false, reason "expired promocode").
    @Test
    public void shouldActivatePromocode() {
        auth.registerNewUser()
                .loginAsCurrentUser()
                .activatePromocode();
    }
}