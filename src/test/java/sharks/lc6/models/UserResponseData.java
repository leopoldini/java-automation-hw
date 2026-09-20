package sharks.lc6.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseData {
    private int id;
    private String token;

    // Java uses camelCase (sessionId), the server sends snake_case (session_id).
    // @JsonProperty tells Jackson which JSON name belongs to this field.
    @JsonProperty("session_id")
    private String sessionId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}
