package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Тело ответа (response body) на регистрацию и логин: то, что сервер ответил НАМ.
 * response.as(AuthResponseDto.class) превращает JSON в объект (десериализация),
 * поэтому фасад пишет auth.getUser().getToken(), а не ищет токен в тексте.
 * Аннотации (одинаковые во всех *ResponseDto):
 *  @NoArgsConstructor - Jackson сначала создаёт пустой объект, потом заполняет поля
 *  @JsonIgnoreProperties - в ответе десятки полей, без неё Jackson упадёт на первом неизвестном
 *  @Getter без сеттеров - значения только читаем; в private поле Jackson пишет сам по @JsonProperty
 *
 * Request DTO используют @Builder: их собираем мы, а response DTO собирает Jackson.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseDto {

    // API возвращает HTTP 200 даже при ошибке, поэтому настоящий результат - это поле status
    @JsonProperty("status")
    private boolean status;

    // Внутри лежит токен для следующих запросов
    @JsonProperty("user")
    private UserResponseDto user;
}