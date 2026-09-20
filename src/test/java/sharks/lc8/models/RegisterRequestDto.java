package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/*
 Body of the register request. Jackson turns this object into the JSON the server expects.
 Builder done by Lombok here - UserDto has the same pattern written by hand, so both
 final fields = once the request is built it cannot be changed.
 */
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

    // Required by the server, irrelevant for the tests - hence fixed defaults
    @JsonProperty("promokey")
    @Builder.Default
    private final String promokey = "";

    // Number 1, not true - that is what the server expects for "terms accepted"
    @JsonProperty("is_accept")
    @Builder.Default
    private final int isAccept = 1;

    // Nested DTO instead of a Map - this is what the reviewer asked for in lecture 6
    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    // UserDto is my own test data, this class is the server's format.
    // Keeping them apart means a change in the API touches only this class.
    public static RegisterRequestDto from(UserDto user) {
        return RegisterRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}