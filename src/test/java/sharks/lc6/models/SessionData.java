package sharks.lc6.models;

public class SessionData {

    private static SessionData instance;
    private String email;
    private String password;
    private String token;

    private SessionData() {}

    public static synchronized SessionData getInstance() {
        if (instance == null) {
            instance = new SessionData();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void clear() {
        this.email = null;
        this.password = null;
        this.token = null;
    }
}