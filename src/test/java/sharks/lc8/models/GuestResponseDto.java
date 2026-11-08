package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Ответ /auth/guest. Аннотации объяснены в AuthResponseDto.
 * Отдельный DTO, потому что ответ плоский - {"token": "..."} - без объекта "user".
 * С AuthResponseDto поле user было null, и getUser().getToken() падал с NullPointerException.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestResponseDto {

    // Гостевой токен: внутри "id": 0, а у токена пользователя - настоящий id
    @JsonProperty("token")
    private String token;
}