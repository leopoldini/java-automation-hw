package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * Body of the login request. Separate class from RegisterRequestDto because login needs
 * fewer fields - no promokey, no is_accept.
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

    // Nested DTO instead of a Map - this is what the reviewer asked for in lecture 6
    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    // UserDto is my own test data, this class is the server's format.
    // Keeping them apart means a change in the API touches only this class.
    public static LoginRequestDto from(UserDto user) {
        return LoginRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}