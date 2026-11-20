package sharks.lc8.tests;

import org.testng.annotations.BeforeMethod;
import sharks.lc8.facade.AuthApiFacade;

/**
 * The parent of the API tests: the setup is moved out of the tests themselves.
 * abstract: it does not run on its own, it is only a parent for tests.
 * A new facade before EVERY test on purpose: the facade remembers the user and the token,
 * and TestNG uses one object of the test class for all its tests -
 * a shared facade would pass the user of one test into the next one.
 */
public abstract class BaseApiTest {

    // protected: available in child tests, for example auth.registerNewUser()
    protected AuthApiFacade auth;

    // Runs before every @Test in the child classes
    @BeforeMethod
    public void setUp() {
        auth = new AuthApiFacade();
    }
}