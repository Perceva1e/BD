package dao;

import model.Client;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void addClient(Client client) throws SQLException {
        String sql = "INSERT INTO \"Clients\" (full_name, phone, passport_number, birth_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getFullName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getPassportNumber());
            stmt.setDate(4, Date.valueOf(client.getBirthDate()));
            stmt.executeUpdate();
        }
    }

    public List<Client> getAllClientsSorted() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM \"Clients\" ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    public Client getClientById(int id) throws SQLException {
        String sql = "SELECT * FROM \"Clients\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToClient(rs) : null;
            }
        }
    }

    public void updateClient(Client client) throws SQLException {
        String sql = """
                UPDATE "Clients" 
                SET full_name = ?, 
                    phone = ?, 
                    passport_number = ?, 
                    birth_date = ? 
                WHERE id = ?""";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getFullName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getPassportNumber());
            stmt.setDate(4, Date.valueOf(client.getBirthDate()));
            stmt.setInt(5, client.getId());
            stmt.executeUpdate();
        }
    }

    public boolean deleteClient(int id) throws SQLException {
        String sql = "DELETE FROM \"Clients\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean clientExists(int clientId) throws SQLException {
        String sql = "SELECT 1 FROM \"Clients\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Client> getClientsWithLuxuryBookings() throws SQLException {
        String sql = """
                SELECT *
                  FROM "Clients"
                  WHERE id IN (
                      SELECT b.client_id
                      FROM "Bookings" b
                      JOIN "Rooms" r ON b.id = r.id
                      JOIN "Room_types" rt ON r.room_type_id = rt.id
                      WHERE rt.name = 'Luxury'
                  );""";

        List<Client> clients = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setFullName(rs.getString("full_name"));
        client.setPhone(rs.getString("phone"));
        client.setPassportNumber(rs.getString("passport_number"));
        client.setBirthDate(rs.getDate("birth_date").toLocalDate());
        return client;
    }
}