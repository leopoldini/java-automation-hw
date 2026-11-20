package sharks.lc8.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Singleton: reads config.properties once and gives the settings to all classes.
// final on the class: nobody can extend it.
public final class ConfigManager {

    // Only the file name. final on the field: the value cannot change. Capital letters = a constant.
    private static final String CONFIG_FILE = "config.properties";

    // Storage for the only object (null at first).
    // volatile: all threads see the fresh value.
    private static volatile ConfigManager instance;

    // A "key = value" table, filled in the constructor.
    private final Properties properties = new Properties();

    // private: new ConfigManager() is not allowed from outside. The file is read here, once.
    private ConfigManager() {
        // try (...) closes the stream by itself after reading (instead of finally { input.close(); }).
        // getResourceAsStream finds the file by name in src/test/resources and opens it as a stream of bytes.
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            // input == null -> the FILE is missing (not the same as instance == null -> no object yet).
            // We get null, not an error, so we fail right away with a clear message.
            if (input == null) {
                throw new IllegalStateException(
                        "File " + CONFIG_FILE + " not found in classpath (src/test/resources)");
            }
            // Copy all "key=value" lines from the file into the properties table
            properties.load(input);
        } catch (IOException e) {
            // The file exists but could not be read. Unchecked: callers do not need their own try-catch.
            // We pass e further, so the real cause is visible in the stack trace.
            throw new IllegalStateException("Failed to read " + CONFIG_FILE, e);
        }
    }

    // The entry point (static): called by the class name, because there is no object yet and new is not allowed.
    public static ConfigManager getInstance() {
        // Check 1 (no lock) for speed: the object already exists -> give it back right away.
        if (instance == null) {
            // Lock: only one thread goes inside, the others wait at the door.
            synchronized (ConfigManager.class) {
                // Check 2 (under the lock): a thread that was waiting will not create a second object.
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        // Always returns the same ConfigManager object
        return instance;
    }

    // First we look at -Dkey=value (a launch parameter), if it is not there - we take the value from the file.
    // The key is nowhere -> fail right away with a clear message instead of passing null further through the code.
    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("Key not found in " + CONFIG_FILE + ": " + key);
        }
        return value;
    }

    // Overloading: the same name get, different parameters.
    // No key -> defaultValue comes back, no error.
    public String get(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }

    // Everything in the file is stored as text: we turn the text into true/false and into a number.
    // String.valueOf: the default value is also turned into text, because get() accepts only String.
    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    // Key names are written only here. IDEA shows a typo in a method name right away,
    // but a typo in the string "api.base.url" shows up only at run time (Key not found).
    public String getApiBaseUrl() {
        return get("api.base.url");
    }

    // The keys baseUrl and headless are shared with the older lecture lc5, so they are not duplicated in the file.
    public String getUiBaseUrl() {
        return get("baseUrl");
    }

    // With a default value: we can live without this key (same for headless and timeout).
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
        return getBoolean("headless", true);
    }

    public int getTimeout() {
        return getInt("ui.timeout", 30000);
    }
}