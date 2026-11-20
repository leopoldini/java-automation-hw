package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The /auth/guest response. The annotations are explained in AuthResponseDto.
 * A separate DTO, because the response is flat - {"token": "..."} - without a "user" object.
 * With AuthResponseDto the user field was null, and getUser().getToken() failed with NullPointerException.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestResponseDto {

    // The guest token: it has "id": 0 inside, while a user token has a real id
    @JsonProperty("token")
    private String token;
}