package sharks.lc8.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;

/*
 Tests for both Builders: the manual one in UserDto and the Lombok one in RegisterRequestDto. No network, they run instantly.
 */
public class BuilderTest {

    // A value we set is kept, a skipped one (type) comes from the default
    @Test
    public void shouldBuildUserWithManualBuilder() {
        UserDto user = UserDto.builder()
                .email("leopold+1@sharkscode.com")
                .password("Test12345")
                .build();

        Assert.assertEquals(user.getEmail(), "leopold+1@sharkscode.com");
        Assert.assertEquals(user.getType(), "email");
    }

    // The check in build(): the test is green only if exactly IllegalStateException is thrown.
    // This is a plus of the manual Builder: Lombok has no such check.
    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldFailWithoutPassword() {
        UserDto.builder()
                .email("leopold+2@sharkscode.com")
                .build();
    }

    // The main point: the object must turn into the JSON the server expects.
    // The snake_case names come from @JsonProperty, the device values are the defaults in DeviceDto.
    // throws Exception: writeValueAsString throws a checked exception, it must be declared or caught.
    @Test
    public void shouldSerializeLombokDtoToJson() throws Exception {
        UserDto user = UserDto.builder()
                .email("leopold+3@sharkscode.com")
                .password("Test12345")
                .build();

        RegisterRequestDto request = RegisterRequestDto.from(user);
        // Jackson: object -> JSON text (serialization)
        String json = new ObjectMapper().writeValueAsString(request);

        // json is the assert message: if the test fails, the whole JSON is visible in the console
        Assert.assertTrue(json.contains("\"is_accept\":1"), json);
        Assert.assertTrue(json.contains("\"device_id\""), json);
        Assert.assertTrue(json.contains("\"user_agent\""), json);
        Assert.assertTrue(json.contains("\"language\":\"uk\""), json);
    }
}