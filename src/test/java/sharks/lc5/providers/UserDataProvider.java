package sharks.lc5.providers;

import org.testng.annotations.DataProvider;

public class UserDataProvider {

    @DataProvider(name = "invalidLoginData")
    public static Object[][] getInvalidLoginData() {
        return new Object[][]{
                {"invalid_email_1@gmail.com", "WrongPassword1!"},
                {"invalid_email_2@gmail.com", "WrongPassword2!"}
        };
    }

    @DataProvider(name = "searchProducts")
    public static Object[][] getSearchProducts() {
        return new Object[][]{
                {"Computer"},
                {"Book"}
        };
    }
}