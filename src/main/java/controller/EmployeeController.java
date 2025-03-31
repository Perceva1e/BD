package controller;

import dao.EmployeeDAO;
import model.Employee;
import service.EmployeeService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
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
    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();
    }

    public Employee getEmployeeById(int id) throws SQLException {
        return employeeDAO.getEmployeeById(id);
    }

    public void deleteEmployee(int id) throws SQLException {
        employeeDAO.deleteEmployee(id);
    }
    public void addEmployee(Employee employee) throws SQLException {
        employeeDAO.addEmployee(employee);
    }

    public void updateEmployee(Employee employee) throws SQLException {
        employeeDAO.updateEmployee(employee);
    }
}