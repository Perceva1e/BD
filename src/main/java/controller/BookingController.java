package controller;

import dao.BookingDAO;
import dao.PaymentDAO; // Добавляем импорт для PaymentDAO
import model.Booking;
import service.BookingService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;

public class BookingController {
    private final BookingService bookingService;
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private final BookingDAO bookingDAO = new BookingDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
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

    public List<Booking> getAllBookings() throws SQLException {
        return bookingDAO.getAllBookings();
    }

    public Booking getBookingById(int id) throws SQLException {
        return bookingDAO.getBookingById(id);
    }

    public void addBooking(Booking booking) throws SQLException {
        bookingDAO.addBooking(booking);
    }

    public void updateBooking(Booking booking) throws SQLException {
        bookingDAO.updateBooking(booking);
    }

    public void deleteBooking(int id) throws SQLException {
        paymentDAO.deletePaymentsByBookingId(id);
        bookingDAO.deleteBooking(id);
    }
}