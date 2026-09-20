package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/*request for "activate promocode". Only one field: {"code": "..."}. */
@Getter
@Builder
public class PromocodeRequestDto {

    @JsonProperty("code")
    private final String code;
}
