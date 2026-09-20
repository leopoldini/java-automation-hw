package sharks.lc8.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import sharks.lc8.config.ConfigManager;

/*
 Parent of the UI tests: opens a browser before every test and closes it after.
 A child class just extends it and gets a ready tab called page.
 abstract = no tests of its own, it only exists to be inherited.
 */
public abstract class BaseUiTest {

    // protected = the child tests work with this tab
    protected Page page;

    // Kept only so tearDown can close them, the tests never touch them
    private Playwright playwright;
    private Browser browser;

    // Before every @Test, so each test starts with a clean browser - no cookies from the previous one
    @BeforeMethod
    public void setUp() {
        ConfigManager config = ConfigManager.getInstance();

        playwright = Playwright.create();
        // headless comes from the config, so -Dui.headless=false shows the window without editing code
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(config.isHeadless()));
        page = browser.newPage();
        // How long Playwright waits for an element before failing
        page.setDefaultTimeout(config.getTimeout());
    }

    @AfterMethod
    public void tearDown() {
        // Null checks in case setUp broke halfway and these were never created.
        // Without closing, browser processes would pile up in the background.
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}