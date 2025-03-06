// RoomDAO.java
package dao;

import model.Room;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.RoomWithTypeDTO;
public class RoomDAO {

    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO \"Rooms\" (area, cost, max_guests, amenities, room_type_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room.getArea());
            stmt.setInt(2, room.getCost());
            stmt.setInt(3, room.getMaxGuests());
            stmt.setString(4, room.getAmenities());
            stmt.setInt(5, room.getRoomTypeId());
            stmt.executeUpdate();
        }
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM \"Rooms\"";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        }
        return rooms;
    }

    public Room getRoomById(int id) throws SQLException {
        String sql = "SELECT * FROM \"Rooms\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToRoom(rs) : null;
            }
        }
    }

    public void updateRoom(Room room) throws SQLException {
        String sql = """
            UPDATE "Rooms" 
            SET area = ?, 
                cost = ?, 
                max_guests = ?, 
                amenities = ?, 
                room_type_id = ? 
            WHERE id = ?""";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room.getArea());
            stmt.setInt(2, room.getCost());
            stmt.setInt(3, room.getMaxGuests());
            stmt.setString(4, room.getAmenities());
            stmt.setInt(5, room.getRoomTypeId());
            stmt.setInt(6, room.getId());
            stmt.executeUpdate();
        }
    }

    public boolean deleteRoom(int id) throws SQLException {
        Integer typeId = getRoomTypeId(id);

        String deleteRoomSql = "DELETE FROM \"Rooms\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteRoomSql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        if (typeId != null) {
            String checkSql = "SELECT COUNT(*) FROM \"Rooms\" WHERE room_type_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setInt(1, typeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    new RoomTypeDAO().deleteRoomType(typeId);
                }
            }
        }
        return true;
    }

    private Integer getRoomTypeId(int roomId) throws SQLException {
        String sql = "SELECT room_type_id FROM \"Rooms\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("room_type_id") : null;
        }
    }
    public boolean deleteRoomsByTypeId(int typeId) throws SQLException {
        String sql = "DELETE FROM \"Rooms\" WHERE room_type_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            return stmt.executeUpdate() > 0;
        }
    }
    public boolean existsRoomType(int typeId) throws SQLException {
        String sql = "SELECT 1 FROM \"Room_types\" WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, typeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean hasRoomsWithType(int typeId) throws SQLException {
        String sql = "SELECT 1 FROM \"Rooms\" WHERE room_type_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, typeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    public List<Room> getRoomsByGuestCapacity(int minGuests) throws SQLException {
        String sql = "SELECT * FROM \"Rooms\" WHERE max_guests > ? ORDER BY area DESC";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minGuests);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        }
        return rooms;
    }

    public List<RoomWithTypeDTO> getRoomsWithTypes() throws SQLException {
        String sql = """
        SELECT r.id, r.area, rt.name, rt.cost_per_night 
        FROM "Rooms" r
        JOIN "Room_types" rt ON r.room_type_id = rt.id
        ORDER BY rt.cost_per_night""";

        List<RoomWithTypeDTO> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(new RoomWithTypeDTO(
                        rs.getInt("id"),
                        rs.getInt("area"),
                        rs.getString("name"),
                        rs.getInt("cost_per_night")
                ));
            }
        }
        return results;
    }
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setArea(rs.getInt("area"));
        room.setCost(rs.getInt("cost"));
        room.setMaxGuests(rs.getInt("max_guests"));
        room.setAmenities(rs.getString("amenities"));
        room.setRoomTypeId(rs.getInt("room_type_id"));
        return room;
    }
}