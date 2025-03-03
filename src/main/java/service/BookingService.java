package services;

import dao.BookingDAO;
import dao.ClientDAO;
import dao.EmployeeDAO;
import validation.InputValidator;
import model.Booking;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
public class BookingService {
    private final BookingDAO bookingDAO;
    private final ClientDAO clientDAO;
    private final EmployeeDAO employeeDAO;
    private final InputValidator inputValidator;
    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.clientDAO = new ClientDAO();
        this.employeeDAO = new EmployeeDAO();
        this.inputValidator = new InputValidator();
    }

    public void addBooking(Scanner scanner) {
        try {
            Booking booking = new Booking();
            LocalDate today = LocalDate.now();

            while (true) {
                try {
                    System.out.print("Check-in date (YYYY-MM-DD): ");
                    LocalDate checkIn = LocalDate.parse(scanner.nextLine());
                    if (checkIn.isBefore(today)) {
                        System.out.println("Check-in date cannot be in the past!");
                        continue;
                    }
                    booking.setCheckInDate(checkIn);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD.");
                }
            }

            while (true) {
                try {
                    System.out.print("Check-out date (YYYY-MM-DD): ");
                    LocalDate checkOut = LocalDate.parse(scanner.nextLine());
                    if (checkOut.isBefore(booking.getCheckInDate())) {
                        System.out.println("Check-out date cannot be earlier than check-in!");
                        continue;
                    }
                    booking.setCheckOutDate(checkOut);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD.");
                }
            }
            String inputs = scanner.nextLine().trim();
            booking.setTotalCost(inputValidator.readPositiveIntInput(inputs,"cost"));

            while (true) {
                System.out.print("Status (PENDING/CONFIRMED/CANCELLED/COMPLETED): ");
                String status = scanner.nextLine().trim().toUpperCase();
                if (inputValidator.isValidStatus(status)) {
                    booking.setStatus(status);
                    break;
                }
                System.out.println("Invalid status! Allowed values: PENDING, CONFIRMED, CANCELLED, COMPLETED");
            }

            while (true) {
                try {
                    System.out.print("Client ID: ");
                    inputs = scanner.nextLine().trim();
                    int clientId = inputValidator.readIntInput(inputs);
                    if (clientDAO.clientExists(clientId)) {
                        booking.setClientId(clientId);
                        break;
                    }
                    System.out.println("Client with ID " + clientId + " does not exist!");
                } catch (Exception e) {
                    System.out.println("Invalid client ID format!");
                }
            }

            while (true) {
                try {
                    System.out.print("Employee ID: ");
                    inputs = scanner.nextLine().trim();
                    int employeeId = inputValidator.readIntInput(inputs);
                    if (employeeDAO.employeeExists(employeeId)) {
                        booking.setEmployeeId(employeeId);
                        break;
                    }
                    System.out.println("Employee with ID " + employeeId + " does not exist!");
                } catch (Exception e) {
                    System.out.println("Invalid employee ID format!");
                }
            }

            bookingDAO.addBooking(booking);
            System.out.println("Booking added successfully!");

        } catch (Exception e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
    }

    public void listBookings() {
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            if (bookings.isEmpty()) System.out.println("\nNo bookings!");
            else bookings.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void updateBooking(Scanner scanner) {
        try {
            System.out.print("Enter booking ID to update: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            Booking existing = bookingDAO.getBookingById(id);
            if (existing == null) {
                System.out.println("Booking not found!");
                return;
            }

            System.out.println("Current booking details:");
            System.out.println(existing);

            while (true) {
                try {
                    System.out.print("New check-in date (YYYY-MM-DD) (press Enter to keep current): ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;

                    LocalDate newCheckIn = LocalDate.parse(input);
                    if (newCheckIn.isBefore(LocalDate.now())) {
                        System.out.println("Check-in date cannot be in the past!");
                        continue;
                    }
                    existing.setCheckInDate(newCheckIn);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD.");
                }
            }

            while (true) {
                try {
                    System.out.print("New check-out date (YYYY-MM-DD) (press Enter to keep current): ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;

                    LocalDate newCheckOut = LocalDate.parse(input);
                    if (newCheckOut.isBefore(existing.getCheckInDate())) {
                        System.out.println("Check-out date cannot be before check-in!");
                        continue;
                    }
                    existing.setCheckOutDate(newCheckOut);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD.");
                }
            }

            System.out.print("New cost (press Enter to keep current): ");
            inputs = scanner.nextLine().trim();
            if (!inputs.isEmpty()) {
                existing.setTotalCost(inputValidator.readPositiveIntInput(inputs, "cost"));
            }

            while (true) {
                System.out.print("New status (PENDING/CONFIRMED/CANCELLED/COMPLETED)(press Enter to keep current): ");
                String status = scanner.nextLine().trim().toUpperCase();
                if (status.isEmpty()) break;
                if (inputValidator.isValidStatus(status)) {
                    existing.setStatus(status);
                    break;
                }
                System.out.println("Invalid status! Allowed values: PENDING, CONFIRMED, CANCELLED, COMPLETED");
            }

            while (true) {
                try {
                    System.out.print("New Client ID (press Enter to keep current): ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;

                    int clientId = Integer.parseInt(input);
                    if (clientDAO.clientExists(clientId)) {
                        existing.setClientId(clientId);
                        break;
                    }
                    System.out.println("Client with ID " + clientId + " does not exist!");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid client ID format!");
                }
            }

            while (true) {
                try {
                    System.out.print("New Employee ID (press Enter to keep current): ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;

                    int employeeId = Integer.parseInt(input);
                    if (employeeDAO.employeeExists(employeeId)) {
                        existing.setEmployeeId(employeeId);
                        break;
                    }
                    System.out.println("Employee with ID " + employeeId + " does not exist!");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid employee ID format!");
                }
            }

            bookingDAO.updateBooking(existing);
            System.out.println("Booking updated successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void deleteBooking(Scanner scanner) {
        try {
            System.out.print("Enter booking ID to delete: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            if (!bookingDAO.bookingExists(id)) {
                System.out.println("Booking not found!");
                return;
            }

            if (bookingDAO.deleteBooking(id)) {
                System.out.println("Booking deleted successfully!");
            } else {
                System.out.println("Failed to delete booking!");
            }
        } catch (Exception e) {
            System.err.println("Error deleting booking: " + e.getMessage());
        }
    }
}