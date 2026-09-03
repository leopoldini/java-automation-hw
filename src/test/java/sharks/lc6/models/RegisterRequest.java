package sharks.lc6.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest {

    @JsonProperty("type")
    private final String type;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("password")
    private final String password;

    @JsonProperty("promokey")
    private final String promokey;

    @JsonProperty("is_accept")
    private final int isAccept;

    @JsonProperty("device")
    private final DeviceData device;

    public RegisterRequest(UserData user) {
        this.type = "email";
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.promokey = "";
        this.isAccept = 1;
        this.device = new DeviceData(); // Ініціалізація поля device
    }

    public String getType() { return type; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPromokey() { return promokey; }
    public int getIsAccept() { return isAccept; }
    public DeviceData getDevice() { return device; }
}