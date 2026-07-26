package sharks.lc1;

public class PasswordValidator {

    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            System.out.println("Error: Password cannot be empty!");
            return;
        }
        if (CommonPassword.LIST.contains(password.toLowerCase())) {
            System.out.println("Error: Password is too common");
            return;
        }
        if (password.length() < 8) {
            System.out.println("Error: Password must be at least 8 characters long!");
            return;
        }
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean specialChar = false;

        for (char ch : password.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                upper = true;
            } else if (Character.isLowerCase(ch)) {
                lower = true;
            } else if (Character.isDigit(ch)) {
                digit = true;
            } else {
                specialChar = true;
            }
        }
        if (!upper) {
            System.out.println("Error: Password must contain at least one uppercase LETTER: ABC...");
        }
        if (!lower) {
            System.out.println("Error: Password must contain at least one lowercase letter: abc");
        }
        if (!digit) {
            System.out.println("Error: Password must contain at least one digit 123...");
        }
        if (!specialChar) {
            System.out.println("Error: Password must contain at least one special character: !@#$...");
        }
        if (password.length() >= 8 && upper && lower && digit && specialChar) {
            System.out.println("Success: Password is valid");
        }
    }
}