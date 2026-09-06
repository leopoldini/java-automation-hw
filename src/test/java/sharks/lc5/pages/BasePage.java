package sharks.lc5.pages;

import com.microsoft.playwright.Page;
import sharks.lc5.utils.LoggerUtil;

import java.util.List;

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
        LoggerUtil.info("Extracted text: '" + text + "'");
        return text;
    }

    protected boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public List<String> getProductTitles(String selector) {
        return page.locator(selector).allInnerTexts();
    }

    public boolean isTitleMatching(List<String> titles, String expectedName) {
        return titles.stream()
                .anyMatch(title -> title.toLowerCase().contains(expectedName.toLowerCase()));
    }
}