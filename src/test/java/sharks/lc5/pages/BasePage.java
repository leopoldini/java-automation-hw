package sharks.lc5.pages;

import com.microsoft.playwright.Page;
import sharks.lc5.utils.LoggerUtil;

public abstract class BasePage {

    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector, String elementName) {
        LoggerUtil.info("Clicking on: " + elementName);
        page.locator(selector).click();
    }

    protected void type(String selector, String value, String elementName) {
        LoggerUtil.info("Typing '" + value + "' into: " + elementName);
        page.locator(selector).fill(value);
    }

    protected String getText(String selector) {
        String text = page.locator(selector).innerText();
        LoggerUtil.info("Extracted text: '" + text + "' from selector: " + selector);
        return text;
    }

    protected boolean isVisible(String selector) {
        boolean visible = page.locator(selector).isVisible();
        LoggerUtil.info("Checking visibility of selector: " + selector + " -> " + visible);
        return visible;
    }
}