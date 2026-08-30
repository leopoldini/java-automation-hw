package sharks.lc5.tests;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import sharks.lc5.config.ConfigManager;
import sharks.lc5.utils.LoggerUtil;

import java.nio.file.Paths;

public class BaseTest {
    protected BrowserContext context;
    protected Page page;

    @AfterMethod
    public void tearDown(ITestResult result) {
        handleTestResult(result);
        closeContext();
    }

    private void handleTestResult(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            LoggerUtil.error("Test FAILED: " + result.getName());
            takeAndAttachScreenshot(result.getName());
        } else if (ITestResult.SUCCESS == result.getStatus()) {
            LoggerUtil.info("Test PASSED: " + result.getName());
        }
    }

    private void takeAndAttachScreenshot(String testName) {
        String screenshotName = testName + "_" + System.currentTimeMillis() + ".png";
        String path = ConfigManager.getProperty("screenshotPath", "build/screenshots/") + screenshotName;

        if (page != null) {
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
            attachScreenshotToAllure(screenshotBytes);
            LoggerUtil.info("Screenshot saved to: " + path);
        }
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] attachScreenshotToAllure(byte[] screenshot) {
        return screenshot;
    }

    private void closeContext() {
        if (context != null) {
            context.close();
        }
    }
}