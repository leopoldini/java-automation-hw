package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/**
 * Page Object: one class per page, selectors and actions live there, tests never see them.
 * This parent holds the browser tab and the actions every page needs, so the child pages
 * do not repeat them. Adding waits or logging to every click would happen here, once.
 *
 * abstract = only real pages (HomePage, LoginPage, RegisterPage) can be created.
 */
public abstract class BasePage {

    // protected = child pages can use it, tests cannot.
    // final + set in the constructor: in my lecture 5 pages this was a settable field and
    // a forgotten page gave NullPointerException. Now a page without a tab cannot exist.
    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        page.click(selector);
    }

    // fill() clears the field before typing
    protected void type(String selector, String value) {
        page.fill(selector, value);
    }

    protected String getText(String selector) {
        return page.locator(selector).innerText();
    }

    // isVisible() answers right now, it does not wait - only use it on a loaded page.
    // first() = take the first match if the selector finds several elements.
    protected boolean isVisible(String selector) {
        return page.locator(selector).first().isVisible();
    }
}