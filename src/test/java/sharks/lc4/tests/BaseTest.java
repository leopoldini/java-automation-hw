package sharks.lc4.tests;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected Properties config = new Properties();

    @BeforeClass
    public void loadConfig() {
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
            config.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playwright = Playwright.create();
        boolean isHeadless = Boolean.parseBoolean(config.getProperty("headless", "true"));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));
    }

    @BeforeMethod
    public void setUp() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate(config.getProperty("baseUrl"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            String screenshotName = result.getName() + "_" + System.currentTimeMillis() + ".png";
            String path = config.getProperty("screenshotPath", "build/screenshots/") + screenshotName;
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
            System.out.println(">>> Test failed! Screenshot saved to: " + path);
        }
        if (context != null) {
            context.close();
        }
    }

    @AfterClass
    public void tearDownClass() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}