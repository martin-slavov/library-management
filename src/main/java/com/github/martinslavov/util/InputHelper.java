package com.github.martinslavov.util;

import java.util.Scanner;

public class InputHelper {

    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input — please enter a number");
            }
        }
    }

    public boolean readConfirmation(String prompt) {
        System.out.print(prompt + " (yes/no): ");
        return scanner.nextLine().trim().equalsIgnoreCase("yes");
    }
}