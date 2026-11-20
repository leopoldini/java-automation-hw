package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * The login request body. The annotations are the same as in RegisterRequestDto.
 * A separate class, because login needs fewer fields: no promokey and no is_accept.
 */
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

    // A nested DTO instead of a Map - the reviewer asked for it in lecture 6
    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    // Turns UserDto (my test data) into the server format
    public static LoginRequestDto from(UserDto user) {
        return LoginRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}