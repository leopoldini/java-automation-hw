package sharks.lc8.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;

/*
 Тесты обоих Builder: ручного в UserDto и Lombok в RegisterRequestDto. Без сети, запускаются мгновенно.
 */
public class BuilderTest {

    // Заданное значение сохраняется, пропущенное (type) берётся по умолчанию
    @Test
    public void shouldBuildUserWithManualBuilder() {
        UserDto user = UserDto.builder()
                .email("leopold+1@sharkscode.com")
                .password("Test12345")
                .build();

        Assert.assertEquals(user.getEmail(), "leopold+1@sharkscode.com");
        Assert.assertEquals(user.getType(), "email");
    }

    // Проверка в build(): тест зелёный, только если брошено именно IllegalStateException.
    // Это плюс ручного Builder: у Lombok такой проверки нет.
    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldFailWithoutPassword() {
        UserDto.builder()
                .email("leopold+2@sharkscode.com")
                .build();
    }

    // Главное: объект должен превратиться в JSON, который ждёт сервер.
    // Имена в snake_case даёт @JsonProperty, значения device - значения по умолчанию в DeviceDto.
    // throws Exception: writeValueAsString бросает checked-исключение, его нужно объявить или поймать.
    @Test
    public void shouldSerializeLombokDtoToJson() throws Exception {
        UserDto user = UserDto.builder()
                .email("leopold+3@sharkscode.com")
                .password("Test12345")
                .build();

        RegisterRequestDto request = RegisterRequestDto.from(user);
        // Jackson: объект -> JSON-текст (сериализация)
        String json = new ObjectMapper().writeValueAsString(request);

        // json - сообщение для assert: если тест упадёт, в консоли будет виден весь JSON
        Assert.assertTrue(json.contains("\"is_accept\":1"), json);
        Assert.assertTrue(json.contains("\"device_id\""), json);
        Assert.assertTrue(json.contains("\"user_agent\""), json);
        Assert.assertTrue(json.contains("\"language\":\"uk\""), json);
    }
}