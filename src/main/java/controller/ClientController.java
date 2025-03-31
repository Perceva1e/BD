package controller;

import dao.ClientDAO;
import model.Client;
import service.ClientService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;

public class ClientController {
    private final ClientService clientService;
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private final ClientDAO clientDAO = new ClientDAO();

    public ClientController() {
        this.clientService = new ClientService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handleClients() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Client Management ===");
            System.out.println("1. Add New Client");
            System.out.println("2. List All Clients");
            System.out.println("3. Update Client");
            System.out.println("4. Delete Client");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);
            switch (choice) {
                case 1 -> clientService.addClient(scanner);
                case 2 -> clientService.listClients();
                case 3 -> clientService.updateClient(scanner);
                case 4 -> clientService.deleteClient(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    public List<Client> getAllClients() throws SQLException {
        return clientDAO.getAllClientsSorted();
    }

    public Client getClientById(int id) throws SQLException {
        return clientDAO.getClientById(id);
    }

    public void addClient(Client client) throws SQLException {
        clientDAO.addClient(client);
    }

    public void updateClient(Client client) throws SQLException {
        clientDAO.updateClient(client);
    }

    public void deleteClient(int id) throws SQLException {
        clientDAO.deleteClient(id);
    }
}
