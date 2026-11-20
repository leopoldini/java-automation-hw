package sharks.lc8.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import sharks.lc8.config.ConfigManager;

/*
 The parent of the UI tests: opens a browser before every test and closes it after.
 A test writes extends BaseUiTest and gets a ready tab page.
 abstract: it does not run on its own, it is only a parent for tests.
 */
public abstract class BaseUiTest {

    // protected: available in child tests, for example new ShopUiFacade(page)
    protected Page page;

    // Kept only to close them in tearDown, the tests do not touch them
    private Playwright playwright;
    private Browser browser;

    // A new browser before EVERY @Test: the test starts without cookies from the previous one.
    // Minus - slower. Faster: one browser and a new BrowserContext (clean profile) for every test.
    @BeforeMethod
    public void setUp() {
        ConfigManager config = ConfigManager.getInstance();

        playwright = Playwright.create();
        // headless=true - no window (faster, this is how CI works), false - with a window.
        // Change it in config.properties: -Dheadless=false will not reach the tests, build.gradle does not pass -D.
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(config.isHeadless()));
        page = browser.newPage();
        // How many milliseconds Playwright waits for an element before it fails (ui.timeout in config.properties)
        page.setDefaultTimeout(config.getTimeout());
    }

    // alwaysRun = true: tearDown runs even if setUp failed in the middle.
    // Without it TestNG would skip tearDown, and the browser would keep hanging in the background.
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // setUp failed before the browser was created -> it is null here, and close() would throw NullPointerException
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
