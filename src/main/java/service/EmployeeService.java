package services;

import dao.EmployeeDAO;
import model.Employee;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;
import java.time.LocalDate;
import java.util.ArrayList;
public class EmployeeService {
    private final EmployeeDAO employeeDAO;
    private final InputValidator inputValidator;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
        this.inputValidator = new InputValidator();
    }
    public void updateEmployee(Scanner scanner) {
        try {
            System.out.print("Enter employee ID to update: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            Employee existing = employeeDAO.getEmployeeById(id);
            if (existing == null) {
                System.out.println("Employee not found!");
                return;
            }

            System.out.println("Current employee details:");
            System.out.println(existing);

            while (true) {
                System.out.print("New full name (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;

                if (input.matches("^[A-Za-z\\s]{3,}$")) {
                    existing.setFullName(input);
                    break;
                }
                System.out.println("Invalid name! Must contain at least 3 letters and spaces only.");
            }

            while (true) {
                System.out.print("New position (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;

                if (input.matches("^[A-Za-z\\s-]{3,}$")) {
                    existing.setPosition(input);
                    break;
                }
                System.out.println("Invalid position! Must contain at least 3 characters.");
            }

            System.out.print("New salary (press Enter to keep current): ");
            inputs = scanner.nextLine().trim();
            if (!inputs.isEmpty()) {
                existing.setSalary(inputValidator.readPositiveIntInput(inputs, "salary"));
            }

            while (true) {
                System.out.print("New work schedule (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;

                try {
                    List<LocalDate> newSchedule = new ArrayList<>();
                    boolean valid = true;
                    LocalDate previousDate = null;
                    LocalDate today = LocalDate.now();

                    for (String dateStr : input.split(",")) {
                        try {
                            LocalDate date = LocalDate.parse(dateStr.trim());

                            if (date.isBefore(today)) {
                                System.out.println("Date cannot be in the past: " + date);
                                valid = false;
                                break;
                            }

                            if (previousDate != null && date.isBefore(previousDate)) {
                                System.out.println("Dates must be in chronological order: "
                                        + previousDate + " ? " + date);
                                valid = false;
                                break;
                            }

                            newSchedule.add(date);
                            previousDate = date;
                        } catch (Exception e) {
                            System.out.println("Invalid date format: " + dateStr);
                            valid = false;
                            break;
                        }
                    }

                    if (valid && !newSchedule.isEmpty()) {
                        existing.setWorkSchedule(newSchedule);
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error processing schedule: " + e.getMessage());
                }
            }

            employeeDAO.updateEmployee(existing);
            System.out.println("Employee updated successfully!");

        } catch (Exception e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
    }

    public void listEmployees() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            if (employees.isEmpty()) System.out.println("\nNo employees!");
            else employees.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void addEmployee(Scanner scanner) {
        try {
            Employee employee = new Employee();

            while (true) {
                System.out.print("Enter full name: ");
                String name = scanner.nextLine().trim();
                if (!name.isEmpty() && name.matches("^[A-Za-z\\s]{3,}$")) {
                    employee.setFullName(name);
                    break;
                }
                System.out.println("Invalid name! Must contain at least 3 letters and spaces only.");
            }

            while (true) {
                System.out.print("Enter position: ");
                String position = scanner.nextLine().trim();
                if (!position.isEmpty() && position.matches("^[A-Za-z\\s-]{3,}$")) {
                    employee.setPosition(position);
                    break;
                }
                System.out.println("Invalid position! Must contain at least 3 characters.");
            }

            String inputs = scanner.nextLine().trim();
            employee.setSalary(inputValidator.readPositiveIntInput(inputs,"salary"));


            List<LocalDate> schedule = new ArrayList<>();
            while (true) {
                try {
                    System.out.print("Enter work schedule (comma-separated dates, YYYY-MM-DD): ");
                    String[] dates = scanner.nextLine().split(",");
                    boolean valid = true;
                    LocalDate previousDate = null;
                    LocalDate today = LocalDate.now();

                    schedule.clear();

                    for (String dateStr : dates) {
                        try {
                            LocalDate date = LocalDate.parse(dateStr.trim());

                            if (date.isBefore(today)) {
                                System.out.println("Date cannot be in the past: " + date);
                                valid = false;
                                break;
                            }

                            if (previousDate != null && date.isBefore(previousDate)) {
                                System.out.println("Dates must be in chronological order: "
                                        + previousDate + " ? " + date);
                                valid = false;
                                break;
                            }

                            schedule.add(date);
                            previousDate = date;
                        } catch (Exception e) {
                            System.out.println("Invalid date format: " + dateStr);
                            valid = false;
                            break;
                        }
                    }

                    if (valid && !schedule.isEmpty()) {
                        employee.setWorkSchedule(schedule);
                        break;
                    }
                    System.out.println("Please enter valid dates in correct order!");
                } catch (Exception e) {
                    System.out.println("Error processing schedule: " + e.getMessage());
                }
            }

            employeeDAO.addEmployee(employee);
            System.out.println("Employee added successfully!");

        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }
    public void deleteEmployee(Scanner scanner) {
        try {
            System.out.print("Enter employee ID to delete: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            if (!employeeDAO.employeeExists(id)) {
                System.out.println("Employee not found!");
                return;
            }
            if (employeeDAO.deleteEmployee(id)) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Failed to delete employee!");
            }
        } catch (Exception e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }
}