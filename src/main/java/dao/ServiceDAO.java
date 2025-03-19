package dao;

import model.Service;
import util.DatabaseConnection;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import dto.ServiceUsageDTO;
import dto.UnusedServiceDTO;

public class ServiceDAO {

    public void addService(Service service) throws SQLException {
        String sql = """
            INSERT INTO "Services" (
                name, 
                category, 
                cost, 
                duration
            ) VALUES (?, ?, ?, ?::interval)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, service.getName());
            stmt.setString(2, service.getCategory());
            stmt.setInt(3, service.getCost());
            stmt.setString(4, formatDuration(service.getDuration()));
            stmt.executeUpdate();
        }
    }

    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM \"Services\" ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }
        return services;
    }

    public void updateService(Service service) throws SQLException {
        String sql = """
            UPDATE "Services" 
            SET name = ?, 
                category = ?, 
                cost = ?, 
                duration = ?::interval 
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, service.getName());
            stmt.setString(2, service.getCategory());
            stmt.setInt(3, service.getCost());
            stmt.setString(4, formatDuration(service.getDuration()));
            stmt.setInt(5, service.getId());
            stmt.executeUpdate();
        }
    }

    public boolean deleteService(int id) throws SQLException {
        String sql = "DELETE FROM \"Services\" WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Service getServiceById(int id) throws SQLException {
        String sql = "SELECT * FROM \"Services\" WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToService(rs) : null;
            }
        }
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" day").append(days != 1 ? "s " : " ");
        }
        sb.append(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        return sb.toString().trim();
    }
    public List<Service> getServicesByDuration(Duration minDuration) throws SQLException {
        String sql = "SELECT * FROM \"Services\" WHERE duration > ?::interval";
        List<Service> services = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, formatDuration(minDuration));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }
        return services;
    }
    public List<UnusedServiceDTO> getUnusedServices() throws SQLException {
        String sql = """
            SELECT s.name, s.category, s.cost
            FROM "Services" s
            LEFT JOIN "Bookings_Services" bs ON s.id = bs.service_id
            WHERE bs.service_id IS NULL
            ORDER BY s.cost DESC""";

        List<UnusedServiceDTO> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new UnusedServiceDTO(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("cost")
                ));
            }
        }
        return results;
    }
    public List<ServiceUsageDTO> getServiceUsageWithDates() throws SQLException {
        String sql = """
            SELECT s.name, s.cost, s.duration, 
                   b.check_in_date, b.check_out_date
            FROM "Services" s
            JOIN "Bookings_Services" bs ON s.id = bs.service_id
            JOIN "Bookings" b ON bs.booking_id = b.id
            ORDER BY s.cost DESC""";

        List<ServiceUsageDTO> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new ServiceUsageDTO(
                        rs.getString("name"),
                        rs.getInt("cost"),
                        parsePgInterval(rs.getString("duration")),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate()
                ));
            }
        }
        return results;
    }
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setId(rs.getInt("id"));
        service.setName(rs.getString("name"));
        service.setCategory(rs.getString("category"));
        service.setCost(rs.getInt("cost"));
        service.setDuration(parsePgInterval(rs.getString("duration")));
        return service;
    }

    private Duration parsePgInterval(String interval) {
        if (interval == null || interval.isEmpty()) return Duration.ZERO;

        try {
            interval = interval.trim().toLowerCase();
            long days = 0;
            long hours = 0;
            long minutes = 0;
            long seconds = 0;

            if (interval.contains("day")) {
                String[] parts = interval.split("day");
                days = Long.parseLong(parts[0].replaceAll("[^0-9]", ""));
                interval = parts.length > 1 ? parts[1] : "";
            }

            String[] timeParts = interval.split(":");
            if (timeParts.length >= 1 && !timeParts[0].isEmpty()) {
                hours = Long.parseLong(timeParts[0].replaceAll("[^0-9]", ""));
            }
            if (timeParts.length >= 2) {
                minutes = Long.parseLong(timeParts[1].replaceAll("[^0-9]", ""));
            }
            if (timeParts.length >= 3) {
                seconds = Long.parseLong(timeParts[2].replaceAll("[^0-9]", ""));
            }

            return Duration.ofDays(days)
                    .plusHours(hours)
                    .plusMinutes(minutes)
                    .plusSeconds(seconds);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid interval format: " + interval, e);
        }
    }
}