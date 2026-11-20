package sharks.lc8.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.config.ConfigManager;

/* Fast tests for the Singleton (ConfigManager). No internet needed. */
public class ConfigManagerTest {

    // The main point of a Singleton: we ask twice and get THE SAME object.
    // assertSame checks "is it the same object", and assertEquals checks "are they equal by content".
    @Test
    public void shouldReturnSameInstance() {
        ConfigManager first = ConfigManager.getInstance();
        ConfigManager second = ConfigManager.getInstance();
        Assert.assertSame(first, second);
    }

    // Proves that config.properties was really read: the values match the file
    @Test
    public void shouldReadPropertiesFromFile() {
        ConfigManager config = ConfigManager.getInstance();
        Assert.assertEquals(config.getApiBaseUrl(), "https://stage.slotcity.ua");
        Assert.assertEquals(config.getDefaultPassword(), "Test12345");
    }
}
