package sharks.lc8.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;

public class BuilderTest {

    @Test
    public void shouldBuildUserWithManualBuilder() {
        UserDto user = UserDto.builder()
                .email("leopold+1@sharkscode.com")
                .password("Test12345")
                .build();

        Assert.assertEquals(user.getEmail(), "leopold+1@sharkscode.com");
        Assert.assertEquals(user.getType(), "email");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldFailWithoutPassword() {
        UserDto.builder()
                .email("leopold+2@sharkscode.com")
                .build();
    }

    @Test
    public void shouldSerializeLombokDtoToJson() throws Exception {
        UserDto user = UserDto.builder()
                .email("leopold+3@sharkscode.com")
                .password("Test12345")
                .build();

        RegisterRequestDto request = RegisterRequestDto.from(user);
        String json = new ObjectMapper().writeValueAsString(request);

        Assert.assertTrue(json.contains("\"is_accept\":1"), json);
        Assert.assertTrue(json.contains("\"device_id\""), json);
        Assert.assertTrue(json.contains("\"user_agent\""), json);
        Assert.assertTrue(json.contains("\"language\":\"uk\""), json);
    }
}