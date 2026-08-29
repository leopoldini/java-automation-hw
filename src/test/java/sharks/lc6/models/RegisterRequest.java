package sharks.lc6.models;

public class RegisterRequest {
    private final String type;
    private final String email;
    private final String password;
    private final String promokey;
    private final int is_accept;
    private final DeviceData device;

    public RegisterRequest(UserData user) {
        this.type = "email";
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.promokey = "";
        this.is_accept = 1;
        this.device = new DeviceData();
    }

    public String getType() { return type; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPromokey() { return promokey; }
    public int getIs_accept() { return is_accept; }
    public DeviceData getDevice() { return device; }
}