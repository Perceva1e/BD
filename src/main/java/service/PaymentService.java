package service;

import dao.PaymentDAO;
import dao.BookingDAO;
import model.Payment;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;
import java.time.LocalDate;
public class PaymentService {
    private final BookingDAO bookingDAO;
    private final PaymentDAO paymentDAO;
    private final InputValidator inputValidator;
    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.inputValidator = new InputValidator();
        this.bookingDAO = new BookingDAO();
    }
    public void addPayment(Scanner scanner) {
        try {
            Payment payment = new Payment();

            while (true) {
                System.out.print("Enter payment type (Credit Card/Debit Card/Cash/Bank Transfer): ");
                String type = scanner.nextLine().trim();
                if (!type.isEmpty() && List.of("Credit Card", "Debit Card", "Cash", "Bank Transfer").contains(type)) {
                    payment.setPaymentType(type);
                    break;
                }
                System.out.println("Invalid payment type! Allowed values: Credit Card, Debit Card, Cash, Bank Transfer");
            }

            while (true) {
                System.out.print("Enter payment date (YYYY-MM-DD): ");
                String dateInput = scanner.nextLine().trim();
                try {
                    LocalDate date = LocalDate.parse(dateInput);
                    if (date.isBefore(LocalDate.now())) {
                        System.out.println("Date cannot be in the past!");
                        continue;
                    }
                    payment.setPaymentDate(date);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD.");
                }
            }

            while (true) {
                System.out.print("Enter payment method (Online/In Person/ATM): ");
                String method = scanner.nextLine().trim();
                if (!method.isEmpty() && List.of("Online", "In Person", "ATM").contains(method)) {
                    payment.setPaymentMethod(method);
                    break;
                }
                System.out.println("Invalid method! Allowed values: Online, In Person, ATM");
            }

            while (true) {
                System.out.print("Enter status (Pending/Completed/Failed/Refunded): ");
                String status = scanner.nextLine().trim();
                if (!status.isEmpty() && List.of("Pending", "Completed", "Failed", "Refunded").contains(status)) {
                    payment.setStatus(status);
                    break;
                }
                System.out.println("Invalid status! Allowed values: Pending, Completed, Failed, Refunded");
            }

            while (true) {
                try {
                    System.out.print("Enter booking ID: ");
                    int bookingId = Integer.parseInt(scanner.nextLine());
                    if (bookingDAO.getBookingById(bookingId) != null) {
                        payment.setBookingId(bookingId);
                        break;
                    }
                    System.out.println("Booking ID does not exist!");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format!");
                } catch (SQLException e) {
                    System.err.println("Database error: " + e.getMessage());
                }
            }

            paymentDAO.addPayment(payment);
            System.out.println("Payment added successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void listPayments() {
        try {
            List<Payment> payments = paymentDAO.getAllPayments();
            if (payments.isEmpty()) System.out.println("\nNo payments!");
            else payments.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void updatePayment(Scanner scanner) {
        try {
            System.out.print("Enter payment ID to update: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            Payment existing = paymentDAO.getPaymentById(id);
            if (existing == null) {
                System.out.println("Payment not found!");
                return;
            }

            System.out.println("Current payment details:");
            System.out.println(existing);


            while(true) {
                System.out.print("New payment type (Credit Card/Debit Card/Cash/Bank Transfer) [Enter to keep]: ");
                String typeInput = scanner.nextLine().trim();
                if (!typeInput.isEmpty()) {
                    if (List.of("Credit Card", "Debit Card", "Cash", "Bank Transfer").contains(typeInput)) {
                        existing.setPaymentType(typeInput);
                    } else {
                        System.out.println("Invalid payment type! Keeping current value.");
                    }
                }
                if (typeInput.isEmpty()) break;
            }


            while(true) {
                System.out.print("New payment date (YYYY-MM-DD) [Enter to keep]: ");
                String dateInput = scanner.nextLine().trim();
                if (dateInput.isEmpty()) break;

                try {
                    LocalDate newDate = LocalDate.parse(dateInput);
                    if (newDate.isBefore(LocalDate.now())) {
                        System.out.println("Date cannot be in the past!");
                        continue;
                    }
                    existing.setPaymentDate(newDate);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD.");
                }
            }


            while(true) {
                System.out.print("New payment method (Online/In Person/ATM) [Enter to keep]: ");
                String methodInput = scanner.nextLine().trim();
                if (!methodInput.isEmpty()) {
                    if (List.of("Online", "In Person", "ATM").contains(methodInput)) {
                        existing.setPaymentMethod(methodInput);
                    } else {
                        System.out.println("Invalid payment method! Keeping current value.");
                    }
                }
                if (methodInput.isEmpty()) break;
            }


            while(true) {
                System.out.print("New status (Pending/Completed/Failed/Refunded) [Enter to keep]: ");
                String statusInput = scanner.nextLine().trim();
                if (!statusInput.isEmpty()) {
                    if (List.of("Pending", "Completed", "Failed", "Refunded").contains(statusInput)) {
                        existing.setStatus(statusInput);
                    } else {
                        System.out.println("Invalid status! Keeping current value.");
                    }
                }
                if (statusInput.isEmpty()) break;
            }


            while(true) {
                System.out.print("New booking ID [Enter to keep]: ");
                String bookingIdInput = scanner.nextLine().trim();
                if (!bookingIdInput.isEmpty()) {
                    try {
                        int newId = Integer.parseInt(bookingIdInput);
                        if (bookingDAO.getBookingById(newId) != null) {
                            existing.setBookingId(newId);
                        } else {
                            System.err.println("Booking ID not found! Keeping current value.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format! Keeping current value.");
                    } catch (SQLException e) {
                        System.err.println("Database error: " + e.getMessage());
                    }
                }
                if (bookingIdInput.isEmpty()) break;
            }
            paymentDAO.updatePayment(existing);
            System.out.println("Payment updated successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void deletePayment(Scanner scanner) {
        try {
            int id;
            while (true) {
                try {
                    System.out.print("Enter payment ID to delete: ");
                    id = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID format!");
                }
            }

            if (paymentDAO.getPaymentById(id) == null) {
                System.out.println("Payment not found!");
                return;
            }

            if (paymentDAO.deletePayment(id)) {
                System.out.println("Payment deleted successfully!");
            } else {
                System.out.println("Deletion failed!");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public List<Payment> getAllPayments() throws SQLException {
        return paymentDAO.getAllPayments();
    }

    public Payment getPaymentById(int id) throws SQLException {
        return paymentDAO.getPaymentById(id);
    }

    public void createPayment(Payment payment) throws SQLException {
        paymentDAO.addPayment(payment);
    }

    public void updatePaymentEntity(Payment payment) throws SQLException {
        paymentDAO.updatePayment(payment);
    }

    public void deletePaymentEntity(int id) throws SQLException {
        paymentDAO.deletePayment(id);
    }
    public boolean isValidBookingId(int bookingId) throws SQLException {
        return bookingDAO.getBookingById(bookingId) != null;
    }
}