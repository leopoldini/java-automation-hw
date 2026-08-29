package sharks.lc6.models;

public class LoginRequest {
    private final String type;
    private final String email;
    private final String password;
    private final DeviceData device;

    public LoginRequest(UserData user) {
        this.type = "email";
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.device = new DeviceData();
    }

    public String getType() { return type; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public DeviceData getDevice() { return device; }
}