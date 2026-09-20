package sharks.lc8.models;

/*
 A test user: email, password, login type. My own simple view of a user - the JSON the server
 wants is built from it later in RegisterRequestDto.from(...) / LoginRequestDto.from(...).
 Builder written by hand (Lombok does the same job in the request DTOs):
 new UserDto("a@b.com", "pass", "email")   <- what is the second argument?
 UserDto.builder().email(...).password(...).build()   <- every value is named
  Also gives a default for what I skip, and one place to validate
 */
public final class UserDto {

    private final String email;
    private final String password;
    private final String type;

    // Private: the builder is the only way in, so build() validation cannot be skipped
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

    // Password left out on purpose so it never reaches the logs
    @Override
    public String toString() {
        return "UserDto{email='" + email + "', type='" + type + "'}";
    }

    // Collects the values one by one. Every method returns this - that is what makes it chainable.
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

        // Last step: a half-filled user fails here, not later inside a request
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