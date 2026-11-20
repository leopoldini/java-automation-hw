package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 The server response to "activate promocode". The annotations are explained in AuthResponseDto.
 status - was the code accepted, reason - why it was refused (for example, "expired promocode").
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromocodeResponseDto {

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("reason")
    private String reason;
}
