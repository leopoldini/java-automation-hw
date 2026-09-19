package sharks.lc8.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.config.ConfigManager;

public class ConfigManagerTest {

    @Test
    public void shouldReturnSameInstance() {
        ConfigManager first = ConfigManager.getInstance();
        ConfigManager second = ConfigManager.getInstance();
        Assert.assertSame(first, second);
    }

    @Test
    public void shouldReadPropertiesFromFile() {
        ConfigManager config = ConfigManager.getInstance();
        Assert.assertEquals(config.getApiBaseUrl(), "https://stage.slotcity.ua");
        Assert.assertEquals(config.getDefaultPassword(), "Test12345");
    }
}