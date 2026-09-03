package sharks.lc6.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    private boolean status;
    private UserResponseData user;

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public UserResponseData getUser() { return user; }
    public void setUser(UserResponseData user) { this.user = user; }
}