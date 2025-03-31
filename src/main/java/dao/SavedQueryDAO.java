package dao;

import model.SavedQuery;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavedQueryDAO {
    public void save(SavedQuery query) throws SQLException {
        String sql = "INSERT INTO saved_queries (name, sql) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, query.getName());
            stmt.setString(2, query.getSql());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) query.setId(rs.getInt(1));
            }
        }
    }

    public List<SavedQuery> findAll() throws SQLException {
        List<SavedQuery> queries = new ArrayList<>();
        String sql = "SELECT * FROM saved_queries ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SavedQuery query = new SavedQuery();
                query.setId(rs.getInt("id"));
                query.setName(rs.getString("name"));
                query.setSql(rs.getString("sql"));
                query.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                queries.add(query);
            }
        }
        return queries;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM saved_queries WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}