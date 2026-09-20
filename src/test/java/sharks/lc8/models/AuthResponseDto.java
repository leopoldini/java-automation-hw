package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 Response DTO: the server answer to register / login.
 response.as(AuthResponseDto.class) turns the JSON into this object, so the facade can write
 auth.getUser().getToken() instead of digging through raw text.
 Why these annotations (same set on every *ResponseDto):
 *  @NoArgsConstructor - Jackson creates an empty object first, then fills the fields
 *  @JsonIgnoreProperties - the real response has dozens of fields; without this Jackson fails
 *  on the first one that is not declared here
 *  @Getter, no setters - these values are only read; @JsonProperty on a private field is enough
 *                        for Jackson to write into it
 *
 * Request DTOs use @Builder instead - we build those ourselves, Jackson builds these.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseDto {

    // This API returns HTTP 200 even on failure, so this flag is the real result
    @JsonProperty("status")
    private boolean status;

    // Holds the token used by the next requests
    @JsonProperty("user")
    private UserResponseDto user;
}