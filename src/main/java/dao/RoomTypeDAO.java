package dao;

import model.RoomType;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO {

    public void addRoomType(RoomType roomType) throws SQLException {
        String sql = """
            INSERT INTO "Room_types" (
                name, 
                comfort_level, 
                category, 
                cost_per_night
            ) VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomType.getName());
            stmt.setString(2, roomType.getComfortLevel());
            stmt.setString(3, roomType.getCategory());
            stmt.setInt(4, roomType.getCostPerNight());
            stmt.executeUpdate();
        }
    }

    public List<RoomType> getAllRoomTypes() throws SQLException {
        List<RoomType> roomTypes = new ArrayList<>();
        String sql = "SELECT * FROM \"Room_types\"";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roomTypes.add(mapResultSetToRoomType(rs));
            }
        }
        return roomTypes;
    }

    public void updateRoomType(RoomType roomType) throws SQLException {
        String sql = """
            UPDATE "Room_types" 
            SET name = ?, 
                comfort_level = ?, 
                category = ?, 
                cost_per_night = ? 
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomType.getName());
            stmt.setString(2, roomType.getComfortLevel());
            stmt.setString(3, roomType.getCategory());
            stmt.setInt(4, roomType.getCostPerNight());
            stmt.setInt(5, roomType.getId());
            stmt.executeUpdate();
        }
    }

    public boolean deleteRoomType(int id) throws SQLException {
        String sql = "DELETE FROM \"Room_types\" WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public RoomType getRoomTypeById(int id) throws SQLException {
        String sql = "SELECT * FROM \"Room_types\" WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToRoomType(rs) : null;
            }
        }
    }
    public List<RoomType> getRoomTypesByPrice(int minPrice) throws SQLException {
        String sql = "SELECT * FROM \"Room_types\" WHERE cost_per_night > ? ORDER BY category";
        List<RoomType> types = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minPrice);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                types.add(mapResultSetToRoomType(rs));
            }
        }
        return types;
    }
    private RoomType mapResultSetToRoomType(ResultSet rs) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setId(rs.getInt("id"));
        roomType.setName(rs.getString("name"));
        roomType.setComfortLevel(rs.getString("comfort_level"));
        roomType.setCategory(rs.getString("category"));
        roomType.setCostPerNight(rs.getInt("cost_per_night"));
        return roomType;
    }
}