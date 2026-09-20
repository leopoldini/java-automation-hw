package sharks.lc8.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import sharks.lc8.models.RegisterRequestDto;
import sharks.lc8.models.UserDto;

/*
 Tests for both builders: the hand-written one in UserDto and the Lombok one in. RegisterRequestDto. No network here, so they run instantly.
 */
public class BuilderTest {

    // The value I set is kept, the one I skipped (type) falls back to its default
    @Test
    public void shouldBuildUserWithManualBuilder() {
        UserDto user = UserDto.builder()
                .email("leopold+1@sharkscode.com")
                .password("Test12345")
                .build();

        Assert.assertEquals(user.getEmail(), "leopold+1@sharkscode.com");
        Assert.assertEquals(user.getType(), "email");
    }

    // The validation inside build() - passes only if that exact exception is thrown.
    // This is the advantage of the hand-written builder: Lombok has no such check.
    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldFailWithoutPassword() {
        UserDto.builder()
                .email("leopold+2@sharkscode.com")
                .build();
    }

    // The real point: the object must become the JSON the server expects.
    // snake_case names come from @JsonProperty, the device values from @Builder.Default -
    // if @Builder.Default were missing, the device fields would be null and this would fail.
    @Test
    public void shouldSerializeLombokDtoToJson() throws Exception {
        UserDto user = UserDto.builder()
                .email("leopold+3@sharkscode.com")
                .password("Test12345")
                .build();

        RegisterRequestDto request = RegisterRequestDto.from(user);
        String json = new ObjectMapper().writeValueAsString(request);

        // json passed as the assert message: on failure the console shows the whole body
        Assert.assertTrue(json.contains("\"is_accept\":1"), json);
        Assert.assertTrue(json.contains("\"device_id\""), json);
        Assert.assertTrue(json.contains("\"user_agent\""), json);
        Assert.assertTrue(json.contains("\"language\":\"uk\""), json);
    }
}