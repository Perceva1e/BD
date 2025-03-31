package controller;

import model.Payment;
import service.PaymentService;

import java.sql.SQLException;
import java.util.List;
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
    public void addPayment(Payment payment) throws SQLException {
        paymentService.createPayment(payment);
    }

    public void updatePayment(Payment payment) throws SQLException {
        paymentService.updatePaymentEntity(payment);
    }

    public List<Payment> getAllPayments() throws SQLException {
        return paymentService.getAllPayments();
    }

    public Payment getPaymentById(int id) throws SQLException {
        return paymentService.getPaymentById(id);
    }

    public void deletePayment(int id) throws SQLException {
        paymentService.deletePaymentEntity(id);
    }
    public boolean isValidBookingId(int bookingId) throws SQLException {
        return paymentService.isValidBookingId(bookingId);
    }
}
