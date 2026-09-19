package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequestDto {

    @JsonProperty("type")
    @Builder.Default
    private final String type = "email";

    @JsonProperty("email")
    private final String email;

    @JsonProperty("password")
    private final String password;

    @JsonProperty("promokey")
    @Builder.Default
    private final String promokey = "";

    @JsonProperty("is_accept")
    @Builder.Default
    private final int isAccept = 1;

    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    public static RegisterRequestDto from(UserDto user) {
        return RegisterRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}