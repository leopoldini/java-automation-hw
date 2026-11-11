package sharks.lc8.pages;

import com.microsoft.playwright.Page;

/*
 Страница, которую магазин открывает сразу после Register ("Your registration completed").
 Это отдельная страница со своим адресом, поэтому у неё свой Page Object. Идея Page Object описана в BasePage.
 */
public class RegistrationResultPage extends BasePage {

    private static final String RESULT_MESSAGE = ".result";

    public RegistrationResultPage(Page page) {
        super(page);
    }

    // Текст сообщения, например "Your registration completed". innerText ждёт, пока сообщение появится.
    public String getResultMessage() {
        return getText(RESULT_MESSAGE);
    }
}
