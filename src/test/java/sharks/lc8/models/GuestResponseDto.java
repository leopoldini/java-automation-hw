package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Answer of /auth/guest. Annotations are explained in AuthResponseDto.
 * A separate DTO and not AuthResponseDto, because this response is flat - {"token": "..."} -
 * with no "user" object around the token. Reusing AuthResponseDto here gave an NPE.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestResponseDto {

    // Guest token: decoded it holds "id": 0, the user token holds the real user id
    @JsonProperty("token")
    private String token;
}