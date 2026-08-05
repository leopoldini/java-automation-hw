package sharks.lc3.helpers;

public class TestHelper {
    public static void logTestStart(String testName) {
        System.out.println(">>> RUNNING TEST: " + testName);
    }
    public static String sanitizePassword(String raw) {
        return raw == null ? "" : raw.trim();
    }
}

