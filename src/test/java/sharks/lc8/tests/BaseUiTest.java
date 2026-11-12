package sharks.lc8.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import sharks.lc8.config.ConfigManager;

/*
 Родитель UI-тестов: открывает браузер перед каждым тестом и закрывает после.
 Тест пишет extends BaseUiTest и получает готовую вкладку page.
 abstract: сам по себе не запускается, это только родитель для тестов.
 */
public abstract class BaseUiTest {

    // protected: доступна в тестах-наследниках, например new ShopUiFacade(page)
    protected Page page;

    // Хранятся только чтобы закрыть их в tearDown, тесты их не трогают
    private Playwright playwright;
    private Browser browser;

    // Новый браузер перед КАЖДЫМ @Test: тест начинается без cookies от предыдущего.
    // Минус - медленнее. Быстрее: один браузер и новый BrowserContext (чистый профиль) на каждый тест.
    @BeforeMethod
    public void setUp() {
        ConfigManager config = ConfigManager.getInstance();

        playwright = Playwright.create();
        // headless=true - без окна (быстрее, так работает CI), false - с окном.
        // Меняется в config.properties: -Dheadless=false до тестов не дойдёт, build.gradle не передаёт -D.
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(config.isHeadless()));
        page = browser.newPage();
        // Сколько миллисекунд Playwright ждёт элемент, прежде чем упасть (ui.timeout в config.properties)
        page.setDefaultTimeout(config.getTimeout());
    }

    // alwaysRun = true: tearDown выполнится, даже если setUp упал посередине.
    // Без него TestNG пропустил бы tearDown, и браузер остался бы висеть в фоне.
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // setUp упал раньше, чем браузер создался -> тут null, и close() дал бы NullPointerException
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
