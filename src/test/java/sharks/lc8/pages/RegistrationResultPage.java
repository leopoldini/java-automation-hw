package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/*
 The page the shop opens right after Register ("Your registration completed").
 It is a separate page with its own address, so it has its own Page Object. The Page Object idea is described in BasePage.
 */
public class RegistrationResultPage extends BasePage {

    private static final String RESULT_MESSAGE = ".result";

    public RegistrationResultPage(Page page) {
        super(page);
    }

    // The message text, for example "Your registration completed". innerText waits until the message appears.
    public String getResultMessage() {
        return getText(RESULT_MESSAGE);
    }
}
