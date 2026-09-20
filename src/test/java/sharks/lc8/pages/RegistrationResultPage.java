package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/*
 The page the shop opens right after we press Register ("Your registration completed").
 It is a separate page with its own URL, so it gets its own Page Object.
 See BasePage for the Page Object idea.
 */
public class RegistrationResultPage extends BasePage {

    private static final String RESULT_MESSAGE = ".result";

    public RegistrationResultPage(Page page) {
        super(page);
    }

    // The message, for example "Your registration completed".
    // innerText waits until the message appears on the page.
    public String getResultMessage() {
        return getText(RESULT_MESSAGE);
    }
}
