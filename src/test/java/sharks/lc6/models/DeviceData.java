package sharks.lc6.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceData {

    @JsonProperty("platform")
    private final String platform;

    @JsonProperty("device_id")
    private final String deviceId;

    @JsonProperty("device_model")
    private final String deviceModel;

    @JsonProperty("browser_name")
    private final String browserName;

    @JsonProperty("browser_version")
    private final String browserVersion;

    @JsonProperty("os_version")
    private final String osVersion;

    @JsonProperty("user_agent")
    private final String userAgent;
        public DeviceData() {
        this.platform = "web";
        this.deviceId = "unique_device_id";
        this.deviceModel = "desktop";
        this.browserName = "Chrome";
        this.browserVersion = "120.0";
        this.osVersion = "Windows 11";
        this.userAgent = "Mozilla/5.0";
    }

    public DeviceData(String platform, String deviceId, String deviceModel,
                      String browserName, String browserVersion,
                      String osVersion, String userAgent) {
        this.platform = platform;
        this.deviceId = deviceId;
        this.deviceModel = deviceModel;
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.osVersion = osVersion;
        this.userAgent = userAgent;
    }

    public String getPlatform() { return platform; }
    public String getDeviceId() { return deviceId; }
    public String getDeviceModel() { return deviceModel; }
    public String getBrowserName() { return browserName; }
    public String getBrowserVersion() { return browserVersion; }
    public String getOsVersion() { return osVersion; }
    public String getUserAgent() { return userAgent; }
}