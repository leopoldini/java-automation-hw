package sharks.lc5.pages;

import com.microsoft.playwright.Page;
import sharks.lc5.utils.LoggerUtil;

import java.util.List;

public abstract class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected String getText(String selector) {
        String text = page.locator(selector).innerText();
        LoggerUtil.info("Extracted text: '" + text + "'");
        return text;
    }

    public List<String> getProductTitles(String selector) {
        return page.locator(selector).allInnerTexts();
    }

    public boolean isTitleMatching(List<String> titles, String expectedName) {
        return titles.stream()
                .anyMatch(title -> title.toLowerCase().contains(expectedName.toLowerCase()));
    }
}