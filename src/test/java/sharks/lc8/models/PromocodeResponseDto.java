package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromocodeResponseDto {

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("includesDepositPromotions")
    private boolean includesDepositPromotions;
}