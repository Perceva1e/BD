package controllers;

import services.PaymentService;
import java.util.Scanner;
import validation.InputValidator;

public class PaymentController {
    private final PaymentService paymentService;
    private final Scanner scanner;
    private final InputValidator inputValidator;
    public PaymentController() {
        this.paymentService = new PaymentService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handlePayments() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Payment Management ===");
            System.out.println("1. Add Payment");
            System.out.println("2. List Payments");
            System.out.println("3. Update Payment");
            System.out.println("4. Delete Payment");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);

            switch (choice) {
                case 1 -> paymentService.addPayment(scanner);
                case 2 -> paymentService.listPayments();
                case 3 -> paymentService.updatePayment(scanner);
                case 4 -> paymentService.deletePayment(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
