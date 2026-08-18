package sharks.lc4.pages;

import com.microsoft.playwright.Page;

public abstract class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void navigateTo(String url) {
        page.navigate(url);
    }
}