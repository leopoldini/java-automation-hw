package sharks.lc6.models;

public class DeviceData {
    private final String platform;
    private final String device_id;
    private final String device_model;
    private final String browser_name;
    private final String browser_version;
    private final String os_version;
    private final String user_agent;
    private final String language;

    public DeviceData() {
        this.platform = "WEB";
        this.device_id = "9b07a82a58d36c9435a40704a8381570";
        this.device_model = "Web Windows Chrome 151";
        this.browser_name = "Chrome";
        this.browser_version = "151.0.0.0";
        this.os_version = "10";
        this.user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
        this.language = "uk";
    }

    public String getPlatform() { return platform; }
    public String getDevice_id() { return device_id; }
    public String getDevice_model() { return device_model; }
    public String getBrowser_name() { return browser_name; }
    public String getBrowser_version() { return browser_version; }
    public String getOs_version() { return os_version; }
    public String getUser_agent() { return user_agent; }
    public String getLanguage() { return language; }
}