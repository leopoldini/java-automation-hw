package sharks.lc5.providers;

import org.testng.annotations.DataProvider;
import sharks.lc5.utils.DataGenerator;

public class UserDataProvider {

    @DataProvider(name = "invalidLoginData")
    public static Object[][] getInvalidLoginData() {
        return new Object[][]{
                {DataGenerator.getRandomEmail(), "WrongPassword1!"},
                {"nonexistent_" + DataGenerator.getRandomEmail(), "Password123!"},
                {DataGenerator.getRandomEmail(), "123"},
                {"unknown_user@domain.com", "Password123!"}
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