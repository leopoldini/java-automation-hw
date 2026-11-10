package sharks.lc8.models;

/*
 Мой тестовый пользователь: email, пароль, тип входа.
 JSON для сервера собирается из него позже: RegisterRequestDto.from(...) / LoginRequestDto.from(...).
 Builder написан вручную: каждое значение подписано, есть значение по умолчанию и проверка в build().
 new UserDto("a@b.com", "pass", "email")               <- что из этого пароль? легко перепутать порядок
 UserDto.builder().email(...).password(...).build()   <- каждое значение подписано
 */
public final class UserDto {

    // final: после создания значения не меняются (immutable), поэтому только геттеры, без сеттеров
    private final String email;
    private final String password;
    private final String type;

    // private: создать можно только через Builder, поэтому проверку в build() не обойти.
    // Сам конструктор ничего не проверяет, только переписывает значения из Builder.
    private UserDto(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.type = builder.type;
    }

    // static: пользователя ещё нет, поэтому вызываем через имя класса. Каждый вызов = новый Builder.
    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    // Пароль не выводим специально, чтобы он не попал в логи
    @Override
    public String toString() {
        return "UserDto{email='" + email + "', type='" + type + "'}";
    }

    // Вложенный класс: живёт внутри UserDto, поэтому может вызвать его private конструктор.
    // static: Builder создаётся раньше пользователя, без объекта UserDto.
    public static final class Builder {

        // Без final: email и password заполняются по шагам.
        private String email;
        private String password;
        // Тип входа всегда "email" (значение по умолчанию), другой тип тестам не нужен.
        private final String type = "email";

        // private: Builder получают только через UserDto.builder()
        private Builder() {
        }

        // return this = тот же Builder, поэтому вызовы идут цепочкой через точку (Fluent)
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        // Последний шаг: проверяем обязательные поля. Полупустой пользователь падает здесь, а не в запросе.
        // isBlank: пустая строка или только пробелы.
        public UserDto build() {
            if (email == null || email.isBlank()) {
                throw new IllegalStateException("email is required");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalStateException("password is required");
            }
            // Builder передаёт себя в private конструктор UserDto
            return new UserDto(this);
        }
    }
}