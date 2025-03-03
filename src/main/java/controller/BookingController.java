package controllers;

import services.BookingService;
import java.util.Scanner;
import validation.InputValidator;
public class BookingController {
    private final BookingService bookingService;
    private final Scanner scanner;
    private final InputValidator inputValidator;
    public BookingController() {
        this.bookingService = new BookingService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handleBookings() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Booking Management ===");
            System.out.println("1. Add New Booking");
            System.out.println("2. List All Bookings");
            System.out.println("3. Update Booking");
            System.out.println("4. Delete Booking");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);

            switch (choice) {
                case 1 -> bookingService.addBooking(scanner);
                case 2 -> bookingService.listBookings();
                case 3 -> bookingService.updateBooking(scanner);
                case 4 -> bookingService.deleteBooking(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}