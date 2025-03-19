package dao;

import model.Booking;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import dto.BookingClientDTO;
import dto.ManagerBookingDTO;
import dto.ClientSpendingDTO;
public class BookingDAO {

    public Booking getBookingById(int id) throws SQLException {
        String sql = "SELECT * FROM \"Bookings\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToBooking(rs) : null;
            }
        }
    }

    public void addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO \"Bookings\" (check_in_date, check_out_date, total_cost, status, client_id, employee_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(booking.getCheckInDate()));
            stmt.setDate(2, Date.valueOf(booking.getCheckOutDate()));
            stmt.setInt(3, booking.getTotalCost());
            stmt.setString(4, booking.getStatus());
            stmt.setInt(5, booking.getClientId());
            stmt.setInt(6, booking.getEmployeeId());
            stmt.executeUpdate();
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM \"Bookings\"";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }

    public void updateBooking(Booking booking) throws SQLException {
        String sql = """
            UPDATE "Bookings" 
            SET check_in_date = ?, 
                check_out_date = ?, 
                total_cost = ?, 
                status = ?, 
                client_id = ?, 
                employee_id = ? 
            WHERE id = ?""";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(booking.getCheckInDate()));
            stmt.setDate(2, Date.valueOf(booking.getCheckOutDate()));
            stmt.setInt(3, booking.getTotalCost());
            stmt.setString(4, booking.getStatus());
            stmt.setInt(5, booking.getClientId());
            stmt.setInt(6, booking.getEmployeeId());
            stmt.setInt(7, booking.getId());
            stmt.executeUpdate();
        }
    }

    public boolean deleteBooking(int id) throws SQLException {
        String sql = "DELETE FROM \"Bookings\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    public boolean deleteBookingsByClientId(int clientId) throws SQLException {
        String sql = "DELETE FROM \"Bookings\" WHERE client_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBookingsByEmployeeId(int employeeId) throws SQLException {
        String sql = "DELETE FROM \"Bookings\" WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            return stmt.executeUpdate() > 0;
        }
    }
    public boolean bookingExists(int id) throws SQLException {
        String sql = "SELECT 1 FROM \"Bookings\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    public List<Booking> getBookingsByClientId(int clientId) throws SQLException {
        String sql = "SELECT * FROM \"Bookings\" WHERE client_id = ?";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }
    public List<Booking> getBookingsByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM \"Bookings\" WHERE status = ? ORDER BY check_in_date";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }

    public List<BookingClientDTO> getBookingsWithClients() throws SQLException {
        String sql = """
        SELECT b.id, b.check_in_date, c.full_name 
        FROM "Bookings" b
        JOIN "Clients" c ON b.client_id = c.id
        ORDER BY b.check_in_date""";

        List<BookingClientDTO> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(new BookingClientDTO(
                        rs.getInt("id"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getString("full_name")
                ));
            }
        }
        return results;
    }
    public void clearEmployeeFromBookings(int employeeId) throws SQLException {
        String sql = "UPDATE \"Bookings\" SET employee_id = NULL WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
        }
    }
    public boolean updateBookingsEmployee(int oldEmployeeId, int newEmployeeId) throws SQLException {
        String sql = "UPDATE \"Bookings\" SET employee_id = ? WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newEmployeeId);
            stmt.setInt(2, oldEmployeeId);
            return stmt.executeUpdate() > 0;
        }
    }
    public List<ManagerBookingDTO> getManagerBookings() throws SQLException {
        String sql = """
            SELECT e.full_name, b.id, b.check_in_date, b.total_cost
            FROM "Bookings" b
            JOIN "Employees" e ON b.employee_id = e.id
            WHERE e.position = 'Manager'
            ORDER BY b.check_in_date""";

        List<ManagerBookingDTO> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new ManagerBookingDTO(
                        rs.getString("full_name"),
                        rs.getInt("id"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getInt("total_cost")
                ));
            }
        }
        return results;
    }
    public List<Booking> getAboveAverageBookings() throws SQLException {
        String sql = "SELECT * FROM \"Bookings\" WHERE total_cost > (SELECT AVG(total_cost) FROM \"Bookings\")";

        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }
    public List<ClientSpendingDTO> getClientSpendingAboveAverage() throws SQLException {
        String sql = """
        SELECT c.id, c.full_name, SUM(b.total_cost) AS total_spent
        FROM "Clients" c
        JOIN "Bookings" b ON c.id = b.client_id
        GROUP BY c.id, c.full_name
        HAVING SUM(b.total_cost) > (SELECT AVG(total_cost) FROM "Bookings")""";

        List<ClientSpendingDTO> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(new ClientSpendingDTO(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getInt("total_spent")
                ));
            }
        }
        return results;
    }
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        booking.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        booking.setTotalCost(rs.getInt("total_cost"));
        booking.setStatus(rs.getString("status"));
        booking.setClientId(rs.getInt("client_id"));
        booking.setEmployeeId(rs.getInt("employee_id"));
        return booking;
    }
}