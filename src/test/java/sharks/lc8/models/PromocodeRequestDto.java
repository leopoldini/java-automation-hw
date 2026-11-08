package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/* Тело запроса "активировать промокод". Одно поле: {"code": "..."}. */
@Getter
@Builder
public class PromocodeRequestDto {

    @JsonProperty("code")
    private final String code;
}
