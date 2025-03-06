package services;

import dao.ClientDAO;
import dao.PaymentDAO;
import dao.BookingDAO;
import model.Client;
import model.Booking;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;
public class ClientService {
    private final BookingDAO bookingDAO;
    private final ClientDAO clientDAO;
    private final PaymentDAO  paymentDAO;
    private final InputValidator inputValidator;

    public ClientService() {
        this.clientDAO = new ClientDAO();
        this.inputValidator = new InputValidator();
        this.bookingDAO = new BookingDAO();
        this.paymentDAO = new PaymentDAO();
    }
    public void addClient(Scanner scanner) {
        try {
            Client client = new Client();

            while (true) {
                System.out.print("Enter full name: ");
                String name = scanner.nextLine().trim();
                if (!name.isEmpty() && name.matches("^[A-Za-z\\s]{3,}$")) {
                    client.setFullName(name);
                    break;
                }
                System.out.println("Invalid name! Must contain at least 3 letters and spaces only.");
            }

            while (true) {
                System.out.print("Enter phone (+375XXXXXXXXXX): ");
                String phone = scanner.nextLine().trim();
                if (phone.matches("^\\+375\\d{9}$")) {
                    client.setPhone(phone);
                    break;
                }
                System.out.println("Invalid phone format! Example: +375295297796");
            }

            while (true) {
                System.out.print("Enter passport number (XXXXXXXXXX): ");
                String passport = scanner.nextLine().trim();
                if (passport.matches("^\\d{10}$")) {
                    client.setPassportNumber(passport);
                    break;
                }
                System.out.println("Invalid passport format! Example: 1234567890");
            }

            while (true) {
                System.out.print("Enter birth date (YYYY-MM-DD): ");
                String dateInput = scanner.nextLine().trim();
                try {
                    LocalDate birthDate = LocalDate.parse(dateInput);
                    if (birthDate.isAfter(LocalDate.now().minusYears(18))) {
                        System.out.println("Client must be at least 18 years old!");
                        continue;
                    }
                    client.setBirthDate(birthDate);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD");
                }
            }

            clientDAO.addClient(client);
            System.out.println("Client added successfully!");
        } catch (Exception e) {
            System.err.println("Error adding client: " + e.getMessage());
        }
    }

    public void listClients() {
        try {
            List<Client> clients = clientDAO.getAllClientsSorted();
            if (clients.isEmpty()) {
                System.out.println("\nNo clients found!");
                return;
            }
            System.out.println("\n=== Client List ===");
            clients.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error retrieving clients: " + e.getMessage());
        }
    }

    public void updateClient(Scanner scanner) {
        try {
            System.out.print("Enter client ID to update: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            Client existingClient = clientDAO.getClientById(id);
            if (existingClient == null) {
                System.out.println("Client not found!");
                return;
            }

            System.out.println("Current client details:");
            System.out.println(existingClient);

            while (true) {
                System.out.print("Enter new full name (press Enter to keep current): ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) break;

                if (name.matches("^[A-Za-z\\s]{3,}$")) {
                    existingClient.setFullName(name);
                    break;
                }
                System.out.println("Invalid name! Must contain at least 3 letters and spaces only.");
            }

            while (true) {
                System.out.print("Enter new phone (+375XXXXXXXXXX) (press Enter to keep current): ");
                String phone = scanner.nextLine().trim();
                if (phone.isEmpty()) break;

                if (phone.matches("^\\+375\\d{9}$")) {
                    existingClient.setPhone(phone);
                    break;
                }
                System.out.println("Invalid phone format! Example: +375295297796");
            }

            while (true) {
                System.out.print("Enter new passport number (XXXXXXXXXX) (press Enter to keep current): ");
                String passport = scanner.nextLine().trim();
                if (passport.isEmpty()) break;

                if (passport.matches("^\\d{10}$")) {
                    existingClient.setPassportNumber(passport);
                    break;
                }
                System.out.println("Invalid passport format! Example: 1234567890");
            }

            while (true) {
                System.out.print("Enter new birth date (YYYY-MM-DD) (press Enter to keep current): ");
                String dateInput = scanner.nextLine().trim();
                if (dateInput.isEmpty()) break;

                try {
                    LocalDate newDate = LocalDate.parse(dateInput);
                    if (newDate.isAfter(LocalDate.now().minusYears(18))) {
                        System.out.println("Client must be at least 18 years old!");
                        continue;
                    }
                    existingClient.setBirthDate(newDate);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Use YYYY-MM-DD");
                }
            }

            clientDAO.updateClient(existingClient);
            System.out.println("Client updated successfully!");
        } catch (Exception e) {
            System.err.println("Error updating client: " + e.getMessage());
        }
    }

    public void deleteClient(Scanner scanner) {
        try {
            System.out.print("Enter client ID to delete: ");
            int id = inputValidator.readIntInput(scanner.nextLine().trim());

            List<Booking> clientBookings = bookingDAO.getBookingsByClientId(id);

            System.out.println("All client, payment and booking will be deleted: ");
            System.out.print("Confirm deletion? (y/n): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("y")) {
                System.out.println("Deletion canceled");
                return;
            }

            for (Booking booking : clientBookings) {
                paymentDAO.deletePaymentsByBookingId(booking.getId());
            }

            bookingDAO.deleteBookingsByClientId(id);

            if (clientDAO.deleteClient(id)) {
                System.out.println("Client, all bookings and related payments deleted!");
            }
        } catch (Exception e) {
            System.err.println("Error deleting client: " + e.getMessage());
        }
    }
}