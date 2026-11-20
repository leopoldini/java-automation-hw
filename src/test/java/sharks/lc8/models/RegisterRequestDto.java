package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/*
 The register request body: what WE send.
 Jackson turns the object into JSON (serialization).
 The Builder here is from Lombok (UserDto has the same one, but written by hand and with a check).
 The fields are final: a built request does not change any more.
 */
@Getter   // Lombok: getters for all fields
@Builder  // Lombok: a Builder without checks
public class RegisterRequestDto {

    // @JsonProperty: the name of the field in JSON.
    // @Builder.Default: a default value that can be changed through the Builder.
    // Without it from() does not compile: the Builder would have no type() method.
    @JsonProperty("type")
    @Builder.Default
    private final String type = "email";

    @JsonProperty("email")
    private final String email;

    @JsonProperty("password")
    private final String password;

    // The server requires this field, the tests do not need it - so an empty string by default
    @JsonProperty("promokey")
    @Builder.Default
    private final String promokey = "";

    // The server expects the number 1, not true ("I accept the terms").
    // In Java the name is isAccept (camelCase), in JSON it is is_accept (snake_case).
    @JsonProperty("is_accept")
    @Builder.Default
    private final int isAccept = 1;

    // A nested DTO instead of a Map - the reviewer asked for it in lecture 6: the fields have types, IDEA sees typos
    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    // Turns UserDto (my test data) into the server format.
    // If the API changes -> I fix only this class, UserDto and the tests stay the same.
    public static RegisterRequestDto from(UserDto user) {
        return RegisterRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}