package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/**
 * Page Object: один класс на одну страницу. Селекторы и действия живут в классе страницы, тесты их не видят.
 * Этот родитель хранит вкладку браузера и действия, нужные каждой странице, чтобы страницы их не повторяли.
 * Добавить ожидание или лог к каждому клику - здесь, в одном месте.
 *
 * abstract: создать можно только настоящие страницы (HomePage, LoginPage...), new BasePage(page) не скомпилируется.
 * Отличие от интерфейса: здесь есть код (поле page и готовые методы).
 */
public abstract class BasePage {

    // Page - вкладка браузера (Playwright). protected: видят страницы-наследники, тесты не видят.
    // final + задаётся в конструкторе: в лекции 5 поле задавали отдельно, забыли -> NullPointerException.
    // Теперь страница без вкладки не может существовать.
    protected final Page page;

    // protected: вызывают только наследники, через super(page)
    protected BasePage(Page page) {
        this.page = page;
    }

    // Методы-помощники: обёртки над Playwright, общие для всех страниц.
    // click, fill и innerText сами ждут элемент (auto-waiting), поэтому в коде нет sleep.
    protected void click(String selector) {
        page.click(selector);
    }

    // fill() сначала очищает поле, потом вводит текст
    protected void type(String selector, String value) {
        page.fill(selector, value);
    }

    protected String getText(String selector) {
        return page.locator(selector).innerText();
    }

    // isVisible() НЕ ждёт, отвечает прямо сейчас - только для уже загруженной страницы.
    // first() = взять первый элемент, если селектор нашёл несколько.
    protected boolean isVisible(String selector) {
        return page.locator(selector).first().isVisible();
    }
}