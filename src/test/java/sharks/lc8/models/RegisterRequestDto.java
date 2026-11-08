package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/*
 Тело запроса регистрации (request body): то, что МЫ отправляем.
 Jackson превращает объект в JSON (сериализация).
 Builder здесь от Lombok (в UserDto такой же, но написан вручную и с проверкой).
 Поля final: собранный запрос уже не меняется.
 */
@Getter   // Lombok: геттеры для всех полей
@Builder  // Lombok: Builder без проверки
public class RegisterRequestDto {

    // @JsonProperty: под каким именем поле идёт в JSON.
    // @Builder.Default: значение по умолчанию, которое можно поменять через Builder.
    // Без неё from() не скомпилируется: у Builder не будет метода type().
    @JsonProperty("type")
    @Builder.Default
    private final String type = "email";

    @JsonProperty("email")
    private final String email;

    @JsonProperty("password")
    private final String password;

    // Сервер требует это поле, тестам оно не нужно - поэтому пустая строка по умолчанию
    @JsonProperty("promokey")
    @Builder.Default
    private final String promokey = "";

    // Сервер ждёт число 1, а не true ("согласен с условиями").
    // В Java имя isAccept (camelCase), в JSON - is_accept (snake_case).
    @JsonProperty("is_accept")
    @Builder.Default
    private final int isAccept = 1;

    // Вложенный DTO вместо Map - так попросил ревьюер в лекции 6: у полей есть типы, IDEA видит опечатки
    @JsonProperty("device")
    @Builder.Default
    private final DeviceDto device = DeviceDto.builder().build();

    // Превращает UserDto (мои тестовые данные) в формат сервера.
    // Поменяется API -> правлю только этот класс, UserDto и тесты не трогаю.
    public static RegisterRequestDto from(UserDto user) {
        return RegisterRequestDto.builder()
                .type(user.getType())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}