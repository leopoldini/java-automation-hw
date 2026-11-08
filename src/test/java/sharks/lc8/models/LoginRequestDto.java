package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * Тело запроса логина (request body). Аннотации - как в RegisterRequestDto.
 * Отдельный класс, потому что логину нужно меньше полей: нет promokey и is_accept.
 */
@Getter
@Builder
public class LoginRequestDto {

    @JsonProperty("type")
    @Builder.Default
    private final String type = "email";

    @JsonProperty("email")
    private final String email;

    @JsonProperty("password")
    private final String password;

    // Вложенный DTO вместо Map - так попросил ревьюер в лекции 6
    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    // Превращает UserDto (мои тестовые данные) в формат сервера
    public static LoginRequestDto from(UserDto user) {
        return LoginRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}