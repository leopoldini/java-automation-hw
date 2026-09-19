package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {

    @JsonProperty("type")
    @Builder.Default
    private final String type = "email";

    @JsonProperty("email")
    private final String email;

    @JsonProperty("password")
    private final String password;

    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    public static LoginRequestDto from(UserDto user) {
        return LoginRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}