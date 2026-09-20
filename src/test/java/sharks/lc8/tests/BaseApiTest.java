package sharks.lc8.tests;

import org.testng.annotations.BeforeMethod;
import sharks.lc8.facade.AuthApiFacade;

/**
 * Parent of the API tests. Keeps the setup out of the tests themselves.
 * A fresh facade before every test on purpose: the facade remembers the current user and token,
 * and TestNG reuses one instance of a test class for all its methods - a shared facade would
 * leak one test's user into the next.
 */
public abstract class BaseApiTest {

    // Used directly in the child tests: auth.registerNewUser()
    protected AuthApiFacade auth;

    // Runs before every @Test in the child classes
    @BeforeMethod
    public void setUp() {
        auth = new AuthApiFacade();
    }
}