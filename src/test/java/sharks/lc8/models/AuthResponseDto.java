package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The response body for register and login: what the server answered US.
 * response.as(AuthResponseDto.class) turns JSON into an object (deserialization),
 * so the facade writes auth.getUser().getToken() instead of searching for the token in text.
 * Annotations (the same in all *ResponseDto):
 *  @NoArgsConstructor - Jackson first creates an empty object, then fills the fields
 *  @JsonIgnoreProperties - the response has dozens of fields, without it Jackson fails on the first unknown one
 *  @Getter without setters - we only read the values; Jackson writes into the private field by itself using @JsonProperty
 *
 * Request DTOs use @Builder: we build them, while response DTOs are built by Jackson.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseDto {

    // The API returns HTTP 200 even on failure, so the real result is the status field
    @JsonProperty("status")
    private boolean status;

    // The token for the next requests is inside
    @JsonProperty("user")
    private UserResponseDto user;
}