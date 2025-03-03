package controllers;

import services.BackUpService;
import java.util.Scanner;
import validation.InputValidator;

public class BackUpController {
    private final BackUpService backUpService;
    private final Scanner scanner;
    private final InputValidator inputValidator;

    public BackUpController() {
        this.backUpService = new BackUpService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handleBackup() {
        System.out.println("\n=== Backup Management ===");
        System.out.println("1. Create Backup");
        System.out.println("2. Restore Backup");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter choice: ");

        String inputs = scanner.nextLine().trim();
        int choice = inputValidator.readIntInput(inputs);

        switch (choice) {
            case 1 -> backUpService.createBackup(scanner);
            case 2 -> backUpService.restoreBackup(scanner);
            case 0 -> {
                return;
            }
            default -> System.out.println("Invalid choice!");
        }
    }
}
