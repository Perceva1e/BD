package services;

import dao.ServiceDAO;
import model.Service;
import java.sql.SQLException;
import java.util.List;
import java.time.Duration;
import java.util.Scanner;
import validation.InputValidator;
public class ServiceService {
    private final ServiceDAO serviceDAO;
    private final InputValidator inputValidator;
    public ServiceService() {
        this.serviceDAO = new ServiceDAO();
        this.inputValidator = new InputValidator();
    }
    public void addService(Scanner scanner) {
        try {
            Service service = new Service();

            while (true) {
                System.out.print("Enter service name: ");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty() && input.matches("^[A-Za-z0-9 \\-]{3,}$")) {
                    service.setName(input);
                    break;
                }
                System.out.println("Invalid name! Minimum 3 characters (letters, numbers, spaces, hyphens)");
            }

            while (true) {
                System.out.print("Enter category: ");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty() && input.matches("^[A-Za-z \\-]{3,}$")) {
                    service.setCategory(input);
                    break;
                }
                System.out.println("Invalid category! Minimum 3 letters (a-z, spaces, hyphens)");
            }

            String inputs = scanner.nextLine().trim();
            service.setCost(inputValidator.readPositiveIntInput(inputs,"cost"));

            while (true) {
                System.out.print("Enter duration (e.g., 1h30m, 45m, 2h, 1 day): ");
                try {
                    service.setDuration(parseDurationInput(scanner.nextLine()));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            serviceDAO.addService(service);
            System.out.println("Service added successfully!");

        } catch (Exception e) {
            System.err.println("Error adding service: " + e.getMessage());
        }
    }

    public String validateNonEmpty(String input, String fieldName) {
        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return input;
    }

    public Duration parseDurationInput(String input) {
        try {
            input = input.trim().toLowerCase()
                    .replaceAll("\\s+", "")
                    .replace("day", "d")
                    .replace("days", "d")
                    .replace("h", "H")
                    .replace("m", "M")
                    .replace("d", "D");

            long days = 0;
            if (input.contains("D")) {
                String[] parts = input.split("D");
                days = Long.parseLong(parts[0]);
                input = parts.length > 1 ? parts[1] : "";
            }

            if (!input.startsWith("PT")) input = "PT" + input;
            Duration duration = Duration.parse(input);
            return duration.plus(Duration.ofDays(days));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid duration format. Examples: 1h30m, 45m, 2h, 1day2h");
        }
    }

    public void listServices() {
        try {
            List<Service> services = serviceDAO.getAllServices();
            if (services.isEmpty()) {
                System.out.println("\nNo services found!");
            } else {
                services.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Error listing services: " + e.getMessage());
        }
    }

    public void updateService(Scanner scanner) {
        try {
            System.out.print("Enter service ID to update: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            Service existing = serviceDAO.getServiceById(id);
            System.out.println("Current service details:");
            System.out.println(existing);
            if (existing == null) {
                System.out.println("Service not found!");
                return;
            }

            Service updatedService = new Service();
            updatedService.setId(id);

            while (true) {
                System.out.print("New name (press Enter to keep current):");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    updatedService.setName(existing.getName());
                    break;
                }
                if (input.matches("^[A-Za-z0-9 \\-]{3,}$")) {
                    updatedService.setName(input);
                    break;
                }
                System.out.println("Invalid name! Minimum 3 characters (letters, numbers, spaces, hyphens)");
            }

            while (true) {
                System.out.print("New category (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    updatedService.setCategory(existing.getCategory());
                    break;
                }
                if (input.matches("^[A-Za-z \\-]{3,}$")) {
                    updatedService.setCategory(input);
                    break;
                }
                System.out.println("Invalid category! Minimum 3 letters (a-z, spaces, hyphens)");
            }

            System.out.print("New cost (press Enter to keep current): ");
            String costInput = scanner.nextLine().trim();
            updatedService.setCost(costInput.isEmpty() ?
                    existing.getCost() :
                    inputValidator.readPositiveIntInput(costInput, "cost"));

            while (true) {
                System.out.print("New duration (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    updatedService.setDuration(existing.getDuration());
                    break;
                }
                try {
                    updatedService.setDuration(parseDurationInput(input));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            serviceDAO.updateService(updatedService);
            System.out.println("Service updated successfully!");

        } catch (Exception e) {
            System.err.println("Error updating service: " + e.getMessage());
        }
    }

    public void deleteService(Scanner scanner) {
        try {
            System.out.print("Enter service ID to delete: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            if (serviceDAO.deleteService(id)) {
                System.out.println("Service deleted successfully!");
            } else {
                System.out.println("Service not found!");
            }
        } catch (Exception e) {
            System.err.println("Error deleting service: " + e.getMessage());
        }
    }
}