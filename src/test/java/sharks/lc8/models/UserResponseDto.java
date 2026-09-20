package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The "user" object nested inside the register / login response.
 * Annotations are explained in AuthResponseDto.
 *
 * Only the fields I actually use are declared here - the server sends far more,
 * @JsonIgnoreProperties drops the rest.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("email")
    private String email;

    // Goes into the authorization header of the next requests
    @JsonProperty("token")
    private String token;
}