package sharks.lc8.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.config.ConfigManager;

/* quick tests for the Singleton (ConfigManager). No internet needed. */
public class ConfigManagerTest {

    // The heart of a Singleton: ask twice, get the SAME object.
    // assertSame compares "is it the very same object", not "do they look alike".
    @Test
    public void shouldReturnSameInstance() {
        ConfigManager first = ConfigManager.getInstance();
        ConfigManager second = ConfigManager.getInstance();
        Assert.assertSame(first, second);
    }

    // Proves the config file (src/test/resources/config.properties) was really read.
    @Test
    public void shouldReadPropertiesFromFile() {
        ConfigManager config = ConfigManager.getInstance();
        Assert.assertEquals(config.getApiBaseUrl(), "https://stage.slotcity.ua");
        Assert.assertEquals(config.getDefaultPassword(), "Test12345");
    }
}
