package sharks.lc6.tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import sharks.lc5.utils.LoggerUtil;
import sharks.lc6.api.SlotCityUserFacade;
import sharks.lc6.api.SlotCityVerifyFacade;
import sharks.lc6.models.AuthResponse;
import sharks.lc6.models.GuestResponse;
import sharks.lc6.models.SessionData;
import sharks.lc6.models.UserData;

import java.util.Random;

public class SlotCityApiE2ETest {

    private final SlotCityUserFacade userFacade = new SlotCityUserFacade();
    private final SlotCityVerifyFacade verifyFacade = new SlotCityVerifyFacade();
    private final SessionData session = new SessionData();

    private final String defaultPassword = "Test12345";
    private final String testPromocode = "D33A922F";

    private String generateEmail() {
        int randomNumber = 100 + new Random().nextInt(900);
        return "leopold+" + randomNumber + "@sharkscode.com";
    }

    @Test
    public void testUserRegistrationLogoutLoginAndPromocodeFlow() {
        UserData user = new UserData(generateEmail(), defaultPassword);

        LoggerUtil.info("--- STEP 1: API Register User ---");
        AuthResponse regResponse = userFacade.registerUser(user, session);
        verifyFacade.verifyRegistrationSuccess(regResponse, session);

        LoggerUtil.info("--- STEP 2: API Log Out (Guest Mode) ---");
        GuestResponse logoutResponse = userFacade.logoutUser(session);
        verifyFacade.verifyLogoutSuccess(logoutResponse, session);

        LoggerUtil.info("--- STEP 3: API Login Created User ---");
        AuthResponse loginResponse = userFacade.loginUser(user, session);
        verifyFacade.verifyLoginSuccess(loginResponse, session);

        LoggerUtil.info("--- STEP 4: API Activate Promocode ---");
        Response promoResponse = userFacade.activatePromocode(session, testPromocode);
        verifyFacade.verifyPromocodeResponse(promoResponse);
    }
}