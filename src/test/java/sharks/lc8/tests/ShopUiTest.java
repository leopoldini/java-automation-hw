package sharks.lc8.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sharks.lc8.config.ConfigManager;
import sharks.lc8.facade.ShopUiFacade;

public class ShopUiTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeMethod
    public void setUp() {
        ConfigManager config = ConfigManager.getInstance();

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(config.isHeadless()));
        page = browser.newPage();
        page.setDefaultTimeout(config.getTimeout());
    }

    @AfterMethod
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    public void shouldRegisterNewUser() {
        new ShopUiFacade(page)
                .openShop()
                .registerNewUser();
    }

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