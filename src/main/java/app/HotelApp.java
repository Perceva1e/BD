package app;

import controller.*;
import ui.panels.*;
import ui.themes.ThemeManager;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import validation.InputValidator;

import javax.swing.*;

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
        ThemeManager.configure();

        if (args.length > 0 && args[0].equals("--gui")) {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Hotel Management System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1280, 720);

                JTabbedPane tabbedPane = new JTabbedPane();

                BookingPanel bookingPanel = new BookingPanel();
                PaymentPanel paymentPanel = new PaymentPanel();
                ClientPanel clientPanel = new ClientPanel(bookingPanel, paymentPanel); // Передаем BookingPanel и PaymentPanel
                RoomPanel roomPanel = new RoomPanel();
                RoomTypePanel roomTypePanel = new RoomTypePanel(roomPanel);
                EmployeePanel employeePanel = new EmployeePanel(bookingPanel);

                roomPanel.setRoomTypePanel(roomTypePanel);
                bookingPanel.setPaymentPanel(paymentPanel);

                tabbedPane.addTab("🏨 Clients", clientPanel);
                tabbedPane.addTab("📅 Bookings", bookingPanel);
                tabbedPane.addTab("👥 Employees", employeePanel);
                tabbedPane.addTab("🛏️ Rooms", roomPanel);
                tabbedPane.addTab("⭐ Room Types", roomTypePanel);
                tabbedPane.addTab("🛎️ Services", new ServicePanel());
                tabbedPane.addTab("💳 Payments", paymentPanel);
                tabbedPane.addTab("📦 Backup", new BackupPanel());
                tabbedPane.addTab("📊 Reports", new ReportPanel());

                try {
                    Connection connection = DatabaseConnection.getConnection();
                    EntityController entityController = new EntityController(connection);
                    tabbedPane.addTab("📋 Dynamic Entities", new EntityManagementPanel(entityController));
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(frame, "Ошибка загрузки панели управления сущностями: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }

                frame.add(tabbedPane);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        try {
                            DatabaseConnection.closeConnection();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(frame, "Ошибка закрытия соединения: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            });
        } else {
            try {
                HotelApp app = new HotelApp();
                app.runConsole();
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
    }

    private void runConsole() {
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