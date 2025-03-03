package controllers;

import services.EmployeeService;
import java.util.Scanner;
import validation.InputValidator;
public class EmployeeController {
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private final InputValidator inputValidator;
    public EmployeeController() {
        this.employeeService = new EmployeeService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handleEmployees() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Employee Management ===");
            System.out.println("1. Add Employee");
            System.out.println("2. List Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);

            switch (choice) {
                case 1 -> employeeService.addEmployee(scanner);
                case 2 -> employeeService.listEmployees();
                case 3 -> employeeService.updateEmployee(scanner);
                case 4 -> employeeService.deleteEmployee(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}