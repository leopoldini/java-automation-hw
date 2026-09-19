package sharks.lc8.tests;

import org.testng.annotations.Test;
import sharks.lc8.api.ApiClient;
import sharks.lc8.api.LoggingApiClient;
import sharks.lc8.api.SlotCityApiClient;
import sharks.lc8.facade.AuthApiFacade;

public class DecoratorTest {

    @Test
    public void shouldLogApiCallsThroughDecorator() {
        ApiClient loggingClient = new LoggingApiClient(new SlotCityApiClient());

        new AuthApiFacade(loggingClient)
                .registerNewUser()
                .loginAsCurrentUser()
                .activatePromocode();
    }
}