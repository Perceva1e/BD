package app;

import controllers.*;
import util.DatabaseConnection;
import java.sql.SQLException;
import java.util.Scanner;
import validation.InputValidator;
public class HotelApp {
    private final Scanner scanner;
    private final ClientController clientController;
    private final BookingController bookingController;
    private final EmployeeController employeeController;
    private final RoomController roomController;
    private final RoomTypeController roomTypeController;
    private final ServiceController serviceController;
    private final PaymentController paymentController;
    private final BackUpController backUpController;
    private final ReportController reportController;
    private final InputValidator inputValidator;
    public HotelApp() {
        this.scanner = new Scanner(System.in);
        this.clientController = new ClientController();
        this.bookingController = new BookingController();
        this.employeeController = new EmployeeController();
        this.roomController = new RoomController();
        this.roomTypeController = new RoomTypeController();
        this.serviceController = new ServiceController();
        this.paymentController = new PaymentController();
        this.backUpController = new BackUpController();
        this.inputValidator = new InputValidator();
        this.reportController = new ReportController();
    }

    public static void main(String[] args) {
        try {
            HotelApp app = new HotelApp();
            app.run();
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        } finally {
            try {
                DatabaseConnection.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);
            switch (choice) {
                case 1 -> clientController.handleClients();
                case 2 -> bookingController.handleBookings();
                case 3 -> employeeController.handleEmployees();
                case 4 -> roomController.handleRooms();
                case 5 -> roomTypeController.handleRoomTypes();
                case 6 -> serviceController.handleServices();
                case 7 -> paymentController.handlePayments();
                case 8 -> backUpController.handleBackup();
                case 9 -> reportController.showReportsMenu();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n=== Hotel Management System ===");
        System.out.println("1. Manage Clients");
        System.out.println("2. Manage Bookings");
        System.out.println("3. Manage Employees");
        System.out.println("4. Manage Rooms");
        System.out.println("5. Manage Room Types");
        System.out.println("6. Manage Services");
        System.out.println("7. Manage Payments");
        System.out.println("8. Manage BackUp");
        System.out.println("9. Reports");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }
}