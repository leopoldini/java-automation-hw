package sharks.lc8.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 Ответ сервера на "активировать промокод". Аннотации объяснены в AuthResponseDto.
 status - принят ли код, reason - почему отказ (например, "expired promocode").
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
