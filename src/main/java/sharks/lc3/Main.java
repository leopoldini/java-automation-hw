package sharks.lc3;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n         Password Validator for DEV merge conflict =( ");
        System.out.print("\nEnter password: ");

        String userPassword = scanner.nextLine();

        System.out.println("\nValidation Result:");
        PasswordValidator.validatePassword(userPassword);

        scanner.close();
    }
}