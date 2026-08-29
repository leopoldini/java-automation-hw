package sharks.lc6.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestResponse {
    private String token;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}