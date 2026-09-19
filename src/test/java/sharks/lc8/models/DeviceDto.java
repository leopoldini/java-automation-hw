package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeviceDto {

    @JsonProperty("platform")
    @Builder.Default
    private final String platform = "WEB";

    @JsonProperty("device_id")
    @Builder.Default
    private final String deviceId = "9b07a82a58d36c9435a40704a8381570";

    @JsonProperty("device_model")
    @Builder.Default
    private final String deviceModel = "Web Windows Chrome 151";

    @JsonProperty("browser_name")
    @Builder.Default
    private final String browserName = "Chrome";

    @JsonProperty("browser_version")
    @Builder.Default
    private final String browserVersion = "151.0.0.0";

    @JsonProperty("os_version")
    @Builder.Default
    private final String osVersion = "10";

    @JsonProperty("user_agent")
    @Builder.Default
    private final String userAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";

    @JsonProperty("language")
    @Builder.Default
    private final String language = "uk";
}