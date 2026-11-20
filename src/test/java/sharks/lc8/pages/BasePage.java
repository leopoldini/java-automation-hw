package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/**
 * Page Object: one class per page. Selectors and actions live in the page class, the tests do not see them.
 * This parent keeps the browser tab and the actions every page needs, so the pages do not repeat them.
 * To add a wait or a log to every click - do it here, in one place.
 *
 * abstract: only real pages can be created (HomePage, LoginPage...), new BasePage(page) does not compile.
 * Difference from an interface: there is code here (the page field and ready methods).
 */
public abstract class BasePage {

    // Page - a browser tab (Playwright). protected: child pages see it, tests do not.
    // final + set in the constructor: in lecture 5 the field was set separately, we forgot it -> NullPointerException.
    // Now a page cannot exist without a tab.
    protected final Page page;

    // protected: only child classes call it, through super(page)
    protected BasePage(Page page) {
        this.page = page;
    }

    // Helper methods: wrappers over Playwright, shared by all pages.
    // click, fill and innerText wait for the element by themselves (auto-waiting), so there is no sleep in the code.
    protected void click(String selector) {
        page.click(selector);
    }

    // fill() first clears the field, then types the text
    protected void type(String selector, String value) {
        page.fill(selector, value);
    }

    protected String getText(String selector) {
        return page.locator(selector).innerText();
    }

    // isVisible() does NOT wait, it answers right now - only for a page that is already loaded.
    // first() = take the first element if the selector found several.
    protected boolean isVisible(String selector) {
        return page.locator(selector).first().isVisible();
    }
}