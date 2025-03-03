package dao;

import model.Employee;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDAO {

    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO \"Employees\" (full_name, position, salary, work_schedule) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getPosition());
            stmt.setInt(3, employee.getSalary());

            if (employee.getWorkSchedule() != null && !employee.getWorkSchedule().isEmpty()) {
                Date[] dates = employee.getWorkSchedule().stream()
                        .map(Date::valueOf)
                        .toArray(Date[]::new);
                stmt.setArray(4, conn.createArrayOf("date", dates));
            } else {
                stmt.setArray(4, null);
            }

            stmt.executeUpdate();
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM \"Employees\"";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        }
        return employees;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = """
        UPDATE "Employees" 
        SET 
            full_name = ?, 
            position = ?, 
            salary = ?, 
            work_schedule = ? 
        WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getPosition());
            stmt.setInt(3, employee.getSalary());

            if (employee.getWorkSchedule() != null && !employee.getWorkSchedule().isEmpty()) {
                Date[] dates = employee.getWorkSchedule().stream()
                        .map(Date::valueOf)
                        .toArray(Date[]::new);
                stmt.setArray(4, conn.createArrayOf("date", dates));
            } else {
                stmt.setArray(4, null);
            }

            stmt.setInt(5, employee.getId());
            stmt.executeUpdate();
        }
    }

    public boolean deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM \"Employees\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Employee getEmployeeById(int id) throws SQLException {
        String sql = "SELECT * FROM \"Employees\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToEmployee(rs) : null;
            }
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setFullName(rs.getString("full_name"));
        employee.setPosition(rs.getString("position"));
        employee.setSalary(rs.getInt("salary"));

        Array scheduleArray = rs.getArray("work_schedule");
        if (scheduleArray != null) {
            Date[] dates = (Date[]) scheduleArray.getArray();
            if (dates != null) {
                List<LocalDate> schedule = Arrays.stream(dates)
                        .map(Date::toLocalDate)
                        .collect(Collectors.toList());
                employee.setWorkSchedule(schedule);
            }
        }
        return employee;
    }
    public boolean employeeExists(int employeeId) throws SQLException {
        String sql = "SELECT id FROM \"Employees\" WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    public List<Employee> getEmployeesBySalary(int minSalary) throws SQLException {
        String sql = "SELECT * FROM \"Employees\" WHERE salary > ? ORDER BY position";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minSalary);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        }
        return employees;
    }
}