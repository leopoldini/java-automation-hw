package sharks.lc8.models;

/*
 My test user: email, password, login type.
 The JSON for the server is built from it later: RegisterRequestDto.from(...) / LoginRequestDto.from(...).
 The Builder is written by hand: every value is labeled, there is a default value and a check in build().
 new UserDto("a@b.com", "pass", "email")               <- which one is the password? easy to mix up the order
 UserDto.builder().email(...).password(...).build()   <- every value is labeled
 */
public final class UserDto {

    // final: the values do not change after creation (immutable), so only getters, no setters
    private final String email;
    private final String password;
    private final String type;

    // private: the only way to create it is the Builder, so the check in build() cannot be skipped.
    // The constructor itself checks nothing, it only copies the values from the Builder.
    private UserDto(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.type = builder.type;
    }

    // static: there is no user yet, so we call it by the class name. Every call = a new Builder.
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

    // The password is left out on purpose, so it does not get into the logs
    @Override
    public String toString() {
        return "UserDto{email='" + email + "', type='" + type + "'}";
    }

    // A nested class: it lives inside UserDto, so it can call its private constructor.
    // static: the Builder is created before the user, without a UserDto object.
    public static final class Builder {

        // No final: email and password are filled step by step.
        private String email;
        private String password;
        // The login type is always "email" (the default value), the tests need no other type.
        private final String type = "email";

        // private: the Builder is available only through UserDto.builder()
        private Builder() {
        }

        // return this = the same Builder, so the calls go in a chain, dot after dot (Fluent)
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        // The last step: check the required fields. A half-empty user fails here, not in the request.
        // isBlank: an empty string or only spaces.
        public UserDto build() {
            if (email == null || email.isBlank()) {
                throw new IllegalStateException("email is required");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalStateException("password is required");
            }
            // The Builder passes itself into the private UserDto constructor
            return new UserDto(this);
        }
    }
}