package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The nested "user" object from the register and login response. The annotations are explained in AuthResponseDto.
 * Only the needed fields are declared, the rest (there are many) are dropped by @JsonIgnoreProperties.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {

    // Goes into the authorization header of the next requests (ApiSpecs.authorized)
    @JsonProperty("token")
    private String token;
}