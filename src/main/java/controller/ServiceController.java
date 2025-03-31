package controller;

import model.Service;
import service.ServiceService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;

public class ServiceController {
    private final ServiceService serviceService;
    private final Scanner scanner;
    private final InputValidator inputValidator;
    public ServiceController() {
        this.serviceService = new ServiceService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handleServices() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Service Management ===");
            System.out.println("1. Add Service");
            System.out.println("2. List Services");
            System.out.println("3. Update Service");
            System.out.println("4. Delete Service");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);
            switch (choice) {
                case 1 -> serviceService.addService(scanner);
                case 2 -> serviceService.listServices();
                case 3 -> serviceService.updateService(scanner);
                case 4 -> serviceService.deleteService(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    public List<Service> getAllServices() throws SQLException {
        return serviceService.getAllServices();
    }

    public Service getServiceById(int id) throws SQLException {
        return serviceService.getServiceById(id);
    }

    public void addService(Service service) throws SQLException {
        serviceService.createService(service);
    }

    public void updateService(Service service) throws SQLException {
        serviceService.updateServiceEntity(service);
    }

    public void deleteService(int id) throws SQLException {
        serviceService.deleteServiceEntity(id);
    }
}
