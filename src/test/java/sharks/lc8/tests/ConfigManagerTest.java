package sharks.lc8.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.config.ConfigManager;

/* Быстрые тесты для Singleton (ConfigManager). Интернет не нужен. */
public class ConfigManagerTest {

    // Главное в Singleton: дважды просим и получаем ТОТ ЖЕ объект.
    // assertSame проверяет "тот же ли это объект", а assertEquals - "равны ли по содержимому".
    @Test
    public void shouldReturnSameInstance() {
        ConfigManager first = ConfigManager.getInstance();
        ConfigManager second = ConfigManager.getInstance();
        Assert.assertSame(first, second);
    }

    // Доказывает, что файл config.properties действительно прочитан: значения совпадают с файлом
    @Test
    public void shouldReadPropertiesFromFile() {
        ConfigManager config = ConfigManager.getInstance();
        Assert.assertEquals(config.getApiBaseUrl(), "https://stage.slotcity.ua");
        Assert.assertEquals(config.getDefaultPassword(), "Test12345");
    }
}
