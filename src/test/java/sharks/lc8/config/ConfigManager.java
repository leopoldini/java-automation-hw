package sharks.lc8.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";

    // volatile = all threads see the newest value (needed for the check inside getInstance)
    private static volatile ConfigManager instance;

    private final Properties properties = new Properties();

    // private constructor = "new ConfigManager()" is impossible. File is loaded here, once.
    private ConfigManager() {
        // try (...) closes the stream automatically
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            // missing file gives null, not an exception - so check it myself
            if (input == null) {
                throw new IllegalStateException(
                        "File " + CONFIG_FILE + " not found in classpath (src/test/resources)");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + CONFIG_FILE, e);
        }
    }

    // Two null checks: first one is fast (object already exists), second one prevents
    // two threads creating two objects at the same moment.
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

    // -Dkey=value wins over the file, so a setting can be changed for one run without editing it.
    // Missing key = fail now, instead of a null travelling through the code.
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

    // Properties file holds only text, so convert it here
    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    // Key names are written only here, so a typo cannot hide anywhere else
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