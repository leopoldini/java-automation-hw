package sharks.lc8.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Singleton: читает config.properties один раз и раздаёт настройки всем классам.
// final у класса: от него нельзя наследоваться.
public final class ConfigManager {

    // Только имя файла. final у поля: значение нельзя поменять. Заглавные буквы = константа.
    private static final String CONFIG_FILE = "config.properties";

    // Хранилище единственного объекта (сначала null).
    // volatile: все потоки видят свежее значение.
    private static volatile ConfigManager instance;

    // Таблица "ключ = значение", заполняется в конструкторе.
    private final Properties properties = new Properties();

    // private: снаружи new ConfigManager() нельзя. Файл читается здесь, один раз.
    private ConfigManager() {
        // try (...) сам закроет поток после чтения (замена finally { input.close(); }).
        // getResourceAsStream ищет файл по имени в src/test/resources и открывает его как поток байтов.
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            // input == null -> ФАЙЛА нет (не путать с instance == null -> объекта нет).
            // Вернётся null, а не ошибка, поэтому падаем сразу с понятным текстом.
            if (input == null) {
                throw new IllegalStateException(
                        "File " + CONFIG_FILE + " not found in classpath (src/test/resources)");
            }
            // Переписываем все строки "ключ=значение" из файла в таблицу properties
            properties.load(input);
        } catch (IOException e) {
            // Файл есть, но прочитать не удалось. Unchecked: вызывающим не нужен свой try-catch.
            // e передаём дальше, чтобы настоящая причина была видна в stack trace.
            throw new IllegalStateException("Failed to read " + CONFIG_FILE, e);
        }
    }

    // Вход (static): вызывается через имя класса, потому что объекта ещё нет, а new запрещён.
    public static ConfigManager getInstance() {
        // Проверка 1 (без замка) для скорости: объект уже есть -> сразу отдаём его.
        if (instance == null) {
            // Замок: внутрь входит только один поток, остальные ждут у двери.
            synchronized (ConfigManager.class) {
                // Проверка 2 (под замком): поток, который ждал, не создаст второй объект.
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        // Всегда возвращает один и тот же объект ConfigManager
        return instance;
    }

    // Сначала смотрим -Dkey=value (параметр запуска), если его нет - берём значение из файла.
    // Ключа нет нигде -> падаем сразу с понятным текстом, а не тащим null дальше по коду.
    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("Key not found in " + CONFIG_FILE + ": " + key);
        }
        return value;
    }

    // Перегрузка (overloading): то же имя get, другие параметры.
    // Ключа нет -> вернётся defaultValue, без ошибки.
    public String get(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }

    // В файле всё хранится как текст: превращаем текст в true/false и в число.
    // String.valueOf: значение по умолчанию тоже переводим в текст, потому что get() принимает только String.
    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    // Имена ключей написаны только здесь. Опечатку в имени метода IDEA покажет сразу,
    // а опечатку в строке "api.base.url" увидим только при запуске (Key not found).
    public String getApiBaseUrl() {
        return get("api.base.url");
    }

    // Ключи baseUrl и headless общие со старой лекцией lc5, поэтому в файле они не дублируются.
    public String getUiBaseUrl() {
        return get("baseUrl");
    }

    // Со значением по умолчанию: без этого ключа можно обойтись (так же у headless и timeout).
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