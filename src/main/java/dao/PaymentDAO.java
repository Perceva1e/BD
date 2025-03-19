package dao;

import model.Payment;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import dto.PaymentBookingDTO;
import dto.PaymentStatsDTO;
import dto.PaymentTypeCountDTO;
public class PaymentDAO {

    public void addPayment(Payment payment) throws SQLException {
        String sql = """
            INSERT INTO "Payments" (
                payment_type, 
                payment_date, 
                payment_method, 
                status, 
                booking_id
            ) VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getPaymentType());
            stmt.setDate(2, Date.valueOf(payment.getPaymentDate()));
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getStatus());
            stmt.setInt(5, payment.getBookingId());
            stmt.executeUpdate();
        }
    }

    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM \"Payments\"";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }
    public boolean deletePaymentsByBookingId(int bookingId) throws SQLException {
        String sql = "DELETE FROM \"Payments\" WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        }
    }

    public void updatePayment(Payment payment) throws SQLException {
        String sql = """
            UPDATE "Payments" 
            SET payment_type = ?, 
                payment_date = ?, 
                payment_method = ?, 
                status = ?, 
                booking_id = ? 
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getPaymentType());
            stmt.setDate(2, Date.valueOf(payment.getPaymentDate()));
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getStatus());
            stmt.setInt(5, payment.getBookingId());
            stmt.setInt(6, payment.getId());
            stmt.executeUpdate();
        }
    }

    public boolean deletePayment(int id) throws SQLException {
        String sql = "DELETE FROM \"Payments\" WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Payment getPaymentById(int id) throws SQLException {
        String sql = "SELECT * FROM \"Payments\" WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToPayment(rs) : null;
            }
        }
    }
    public List<Payment> getPaymentsByPeriod(double months) throws SQLException {
        String sql = "SELECT * FROM \"Payments\" WHERE payment_date >= CURRENT_DATE - INTERVAL '1 MONTH' * ?";
        List<Payment> payments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, months);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    public List<PaymentBookingDTO> getCreditCardPayments() throws SQLException {
        String sql = """
        SELECT p.id, p.payment_date, b.total_cost, b.status 
        FROM "Payments" p
        JOIN "Bookings" b ON p.booking_id = b.id
        WHERE p.payment_type = 'Credit Card'
        ORDER BY p.payment_date DESC""";

        List<PaymentBookingDTO> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(new PaymentBookingDTO(
                        rs.getInt("id"),
                        rs.getDate("payment_date").toLocalDate(),
                        rs.getInt("total_cost"),
                        rs.getString("status")
                ));
            }
        }
        return results;
    }
    public List<PaymentStatsDTO> getPaymentStats() throws SQLException {
        String sql = """
        SELECT payment_method, status, COUNT(*) as count
        FROM "Payments"
        GROUP BY payment_method, status""";

        List<PaymentStatsDTO> stats = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stats.add(new PaymentStatsDTO(
                        rs.getString("payment_method"),
                        rs.getString("status"),
                        rs.getInt("count")
                ));
            }
        }
        return stats;
    }
    public List<PaymentTypeCountDTO> getPaymentTypeCounts() throws SQLException {
        String sql = "SELECT payment_type, COUNT(*) AS count FROM \"Payments\" GROUP BY payment_type";

        List<PaymentTypeCountDTO> counts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                counts.add(new PaymentTypeCountDTO(
                        rs.getString("payment_type"),
                        rs.getInt("count")
                ));
            }
        }
        return counts;
    }
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setPaymentType(rs.getString("payment_type"));
        payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setStatus(rs.getString("status"));
        payment.setBookingId(rs.getInt("booking_id"));
        return payment;
    }
}