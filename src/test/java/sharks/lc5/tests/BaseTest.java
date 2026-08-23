package sharks.lc5.tests;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import sharks.lc5.config.ConfigManager;
import sharks.lc5.utils.LoggerUtil;

import java.nio.file.Paths;

public abstract class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    public void setUpClass() {
        LoggerUtil.info("=== STARTING TEST SUITE EXECUTION ===");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(ConfigManager.isHeadless())
        );
    }

    @BeforeMethod
    public void setUp() {
        context = browser.newContext();
        page = context.newPage();
        LoggerUtil.info("Navigating to: " + ConfigManager.getBaseUrl());
        page.navigate(ConfigManager.getBaseUrl());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        processTestResult(result);

        if (context != null) {
            context.close();
        }
    }

    private void processTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            handleFailure(result);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            handleSuccess(result);
        }
    }

    private void handleFailure(ITestResult result) {
        LoggerUtil.error("Test FAILED: " + result.getName());
        takeScreenshot(result.getName());
    }

    private void handleSuccess(ITestResult result) {
        LoggerUtil.info("Test PASSED: " + result.getName());
    }

    private void takeScreenshot(String testName) {
        String screenshotName = testName + "_" + System.currentTimeMillis() + ".png";
        String path = ConfigManager.getProperty("screenshotPath", "build/screenshots/") + screenshotName;
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
        LoggerUtil.info("Screenshot saved to: " + path);
    }

    @AfterClass
    public void tearDownClass() {
        LoggerUtil.info("=== FINISHING TEST SUITE EXECUTION ===");
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}