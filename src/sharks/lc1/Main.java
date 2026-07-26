package sharks.lc1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n         Password Validator for Sharkscode =)");
        System.out.print("\nEnter password: ");

        String userPassword = scanner.nextLine();

        System.out.println("\nValidation Result:");
        PasswordValidator.validatePassword(userPassword);

        scanner.close();
    }
}