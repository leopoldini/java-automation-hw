package sharks.lc3;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sharks.lc3.helpers.TestHelper;

public class PasswordValidatorTest {

    private static final int INITIAL_COLLECTION_SIZE = 0;
    private static final int EXPECTED_SIZE_AFTER_ADDITIONS = 2;
    private static final int EXPECTED_SIZE_AFTER_REMOVAL = 0;

    private PasswordHistoryManager historyManager;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        System.out.println("[BEFORE METHOD] Initializing test environment...");
        historyManager = new PasswordHistoryManager();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        System.out.println("[AFTER METHOD] Cleaning up history collection...");
        historyManager.clearHistory();
    }

    @Test(dataProvider = "validPasswords", groups = {"smoke", "regression"})
    public void testValidPasswords(String password) {
        TestHelper.logTestStart("testValidPasswords with: " + password);
        boolean result = PasswordValidator.isValid(TestHelper.sanitizePassword(password));
        Assert.assertTrue(result, "Password should be valid: " + password);
    }

    @Test(dataProvider = "invalidPasswords", groups = {"regression"})
    public void testInvalidPasswords(String password) {
        TestHelper.logTestStart("testInvalidPasswords with: " + password);
        boolean result = PasswordValidator.isValid(TestHelper.sanitizePassword(password));
        Assert.assertFalse(result, "Password should be invalid: " + password);
    }

    @Test(groups = {"regression"})
    public void testNullPassword() {
        TestHelper.logTestStart("testNullPassword");
        Assert.assertFalse(PasswordValidator.isValid(null), "Null password should be invalid");
    }

    @Test(groups = {"collection", "smoke"})
    public void testCollectionAddAndSize() {
        TestHelper.logTestStart("testCollectionAddAndSize");
        Assert.assertEquals(historyManager.getSize(), INITIAL_COLLECTION_SIZE, "Initial size should be 0");

        historyManager.addPassword("P@ssword1");
        historyManager.addPassword("Secure#2026");

        Assert.assertEquals(historyManager.getSize(), EXPECTED_SIZE_AFTER_ADDITIONS, "Collection size should be 2 after additions");
    }

    @Test(groups = {"collection"})
    public void testCollectionContains() {
        TestHelper.logTestStart("testCollectionContains");
        String pass = "P@ssword1";
        historyManager.addPassword(pass);

        Assert.assertTrue(historyManager.containsPassword(pass), "Collection must contain added password");
        Assert.assertFalse(historyManager.containsPassword("NonExisting1!"), "Collection should not contain non-added password");
    }

    @Test(groups = {"collection"})
    public void testCollectionRemove() {
        TestHelper.logTestStart("testCollectionRemove");
        String pass = "P@ssword1";
        historyManager.addPassword(pass);

        boolean isRemoved = historyManager.removePassword(pass);

        Assert.assertTrue(isRemoved, "Password should be successfully removed");
        Assert.assertEquals(historyManager.getSize(), EXPECTED_SIZE_AFTER_REMOVAL, "Size should be 0 after removal");
        Assert.assertFalse(historyManager.containsPassword(pass), "Removed password should no longer exist in collection");
    }

    @DataProvider(name = "validPasswords")
    public Object[][] validPasswordProvider() {
        return new Object[][]{
                {"P@ssword1"},
                {"Secure#2026"},
                {"MyComplex123!"}
        };
    }

    @DataProvider(name = "invalidPasswords")
    public Object[][] invalidPasswordProvider() {
        return new Object[][]{
                {""},
                {"Admin"},
                {"12345678"},
                {"password123!"},
                {"PASSWORD123!"},
                {"Password!"},
                {"Password123"}
        };
    }
}