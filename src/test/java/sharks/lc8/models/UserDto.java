package sharks.lc8.models;

public final class UserDto {

    private final String email;
    private final String password;
    private final String type;

    private UserDto(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.type = builder.type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "UserDto{email='" + email + "', type='" + type + "'}";
    }

    public static final class Builder {

        private String email;
        private String password;
        private String type = "email";

        private Builder() {
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public UserDto build() {
            if (email == null || email.isBlank()) {
                throw new IllegalStateException("email is required");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalStateException("password is required");
            }
            return new UserDto(this);
        }
    }
}