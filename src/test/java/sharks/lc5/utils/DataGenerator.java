package sharks.lc5.utils;

import java.util.UUID;

public class DataGenerator {

    private static final int EMAIL_PREFIX_LENGTH = 8;
    private static final int NAME_PREFIX_LENGTH = 4;

    public static String getRandomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, EMAIL_PREFIX_LENGTH) + "@test.com";
    }

    public static String getRandomFirstName() {
        return "FirstName_" + UUID.randomUUID().toString().substring(0, NAME_PREFIX_LENGTH);
    }

    public static String getRandomLastName() {
        return "LastName_" + UUID.randomUUID().toString().substring(0, NAME_PREFIX_LENGTH);
    }
}