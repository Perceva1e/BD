package controller;

import service.ReportService;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;
public class ReportController {
    private final ReportService reportService;
    private final Scanner scanner;
    private final InputValidator inputValidator;

    public ReportController() {
        this.reportService = new ReportService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void showReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Reports Menu ===");
            System.out.println("1. High-Paid Employees (>50k)");
            System.out.println("2. All Clients (Sorted)");
            System.out.println("3. Active Bookings");
            System.out.println("4. Bookings with Client Info");
            System.out.println("5. Recent Payments (Last 1.7 months)");
            System.out.println("6. Credit Card Payments");
            System.out.println("7. Expensive Room Types (>1500)");
            System.out.println("8. Large Capacity Rooms (>2 guests)");
            System.out.println("9. Rooms with Type Info");
            System.out.println("10. Long Services (>1 hour)");
            System.out.println("11. Unused Services Report");
            System.out.println("12. Manager Bookings Report");
            System.out.println("13. Service Usage Timeline");
            System.out.println("14. High Average Paid Employees");
            System.out.println("15. Clients with Luxury Bookings");
            System.out.println("16. Room Type Cost Stats");
            System.out.println("17. Payment Statistics");
            System.out.println("18. Rooms Above Average Area");
            System.out.println("19. Rooms with Kitchen/Jacuzzi");
            System.out.println("20. Service Costs by Category");
            System.out.println("21. Expensive Bookings");
            System.out.println("22. Big Spender Clients");
            System.out.println("23. Payment Type Counts");
            System.out.println("24. Kitchen/Jacuzzi Rooms");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);
            try {
                switch (choice) {
                    case 1 -> printReport("High-Paid Employees", reportService.getHighPaidEmployees());
                    case 2 -> printReport("All Clients", reportService.getAllClientsSorted());
                    case 3 -> printReport("Active Bookings", reportService.getActiveBookings());
                    case 4 -> printReport("Bookings with Clients", reportService.getBookingsWithClients());
                    case 5 -> printReport("Recent Payments", reportService.getRecentPayments());
                    case 6 -> printReport("Credit Card Payments", reportService.getCreditCardPayments());
                    case 7 -> printReport("Expensive Room Types", reportService.getExpensiveRoomTypes());
                    case 8 -> printReport("Large Capacity Rooms", reportService.getLargeCapacityRooms());
                    case 9 -> printReport("Rooms with Types", reportService.getRoomsWithTypes());
                    case 10 -> printReport("Long Services", reportService.getLongServices());
                    case 11 -> printReport("Unused Services Report",reportService.printUnusedServices());
                    case 12 -> printReport("Manager Bookings Report",reportService.printManagerBookings());
                    case 13 -> printReport("Service Usage Timeline",reportService.printServiceUsage());
                    case 14 -> printReport("High Average Paid Employees", reportService.getHighAveragePaidEmployees());
                    case 15 -> printReport("Clients with Luxury", reportService.getClientsWithLuxuryBookings());
                    case 16 -> printReport("Room Type Costs", reportService.getRoomTypeCostStats());
                    case 17 -> printReport("Payment Stats", reportService.getPaymentStats());
                    case 18 -> printReport("Large Rooms", reportService.getRoomsAboveAverageArea());
                    case 19 -> printReport("Special Amenities", reportService.getRoomsWithAmenities());
                    case 20 -> printReport("Service Costs", reportService.getServiceCostByCategory());
                    case 21 -> printReport("Expensive Bookings", reportService.getAboveAverageBookings());
                    case 22 -> printReport("Big Spenders", reportService.getClientSpendingAboveAverage());
                    case 23 -> printReport("Payment Types", reportService.getPaymentTypeCounts());
                    case 24 -> printReport("Special Rooms", reportService.getRoomsWithKitchenOrJacuzzi());
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.err.println("Error generating report: " + e.getMessage());
            }
        }
    }

    private <T> void printReport(String title, List<T> items) {
        System.out.println("\n=== " + title + " ===");
        if (items.isEmpty()) {
            System.out.println("No results found");
        } else {
            items.forEach(System.out::println);
        }
    }

}