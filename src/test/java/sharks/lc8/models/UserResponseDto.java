package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Вложенный объект "user" из ответа на регистрацию и логин. Аннотации объяснены в AuthResponseDto.
 * Объявлены только нужные поля, остальные (их много) отбрасывает @JsonIgnoreProperties.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {

    // Уходит в заголовок authorization следующих запросов (ApiSpecs.authorized)
    @JsonProperty("token")
    private String token;
}