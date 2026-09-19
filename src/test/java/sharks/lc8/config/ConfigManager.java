package sharks.lc8.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";

    private static volatile ConfigManager instance;

    private final Properties properties = new Properties();

    private ConfigManager() {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new IllegalStateException(
                        "File " + CONFIG_FILE + " not found in classpath (src/test/resources)");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + CONFIG_FILE, e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("Key not found in " + CONFIG_FILE + ": " + key);
        }
        return value;
    }

    public String get(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    public String getApiBaseUrl() {
        return get("api.base.url");
    }

    public String getUiBaseUrl() {
        return get("ui.base.url");
    }

    public String getLocalization() {
        return get("api.localization", "ua");
    }

    public String getDefaultPassword() {
        return get("default.password");
    }

    public String getPromocode() {
        return get("test.promocode");
    }

    public boolean isHeadless() {
        return getBoolean("ui.headless", true);
    }

    public int getTimeout() {
        return getInt("ui.timeout", 30000);
    }
}