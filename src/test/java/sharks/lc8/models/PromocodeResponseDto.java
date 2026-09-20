package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 server's answer to "activate promocode".
 "status" says whether the code was accepted, "reason" says why it was refused.
 (What the annotations mean is explained in AuthResponseDto.)
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
