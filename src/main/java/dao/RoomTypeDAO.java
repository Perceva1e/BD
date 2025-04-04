package dao;

import model.RoomType;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.RoomTypeCostDTO;

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

        String deleteRoomsSql = "DELETE FROM \"Rooms\" WHERE room_type_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteRoomsSql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        String deleteTypeSql = "DELETE FROM \"Room_types\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteTypeSql)) {
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
    public List<RoomTypeCostDTO> getMinMaxCostByCategory() throws SQLException {
        String sql = """
        SELECT category, 
               MIN(cost_per_night) as min_cost, 
               MAX(cost_per_night) as max_cost
        FROM "Room_types"
        GROUP BY category""";

        List<RoomTypeCostDTO> stats = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stats.add(new RoomTypeCostDTO(
                        rs.getString("category"),
                        rs.getInt("min_cost"),
                        rs.getInt("max_cost")
                ));
            }
        }
        return stats;
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