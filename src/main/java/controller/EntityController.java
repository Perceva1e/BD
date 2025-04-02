package controller;

import model.DynamicEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityController {
    private final Connection connection;

    public EntityController(Connection connection) {
        this.connection = connection;
    }

    public void createEntity(DynamicEntity entity) throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(entity.getName().toLowerCase()).append(" (");
        for (Map.Entry<String, String> field : entity.getFields().entrySet()) {
            sql.append(field.getKey()).append(" ").append(field.getValue()).append(", ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            stmt.executeUpdate();
        }
    }

    public void deleteEntity(String entityName) throws SQLException {
        String sql = "DROP TABLE " + entityName.toLowerCase();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public List<DynamicEntity> getEntities() throws SQLException {
        List<DynamicEntity> entities = new ArrayList<>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                DynamicEntity entity = new DynamicEntity(tableName);
                String columnsSql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = ?";
                try (PreparedStatement columnsStmt = connection.prepareStatement(columnsSql)) {
                    columnsStmt.setString(1, tableName);
                    try (ResultSet columnsRs = columnsStmt.executeQuery()) {
                        while (columnsRs.next()) {
                            String columnName = columnsRs.getString("column_name");
                            String dataType = columnsRs.getString("data_type");
                            if (!columnName.equalsIgnoreCase("ID")) {
                                String mappedType = mapDataType(dataType);
                                entity.addField(columnName, mappedType);
                            }
                        }
                    }
                }
                entities.add(entity);
            }
        }
        return entities;
    }

    public void addRecord(String entityName, Map<String, Object> record) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(entityName.toLowerCase()).append(" (");
        for (String key : record.keySet()) {
            sql.append(key).append(", ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(") VALUES (");
        for (int i = 0; i < record.size(); i++) {
            sql.append("?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Object value : record.values()) {
                stmt.setObject(paramIndex++, value);
            }
            stmt.executeUpdate();
        }
    }

    public void updateRecord(String entityName, Map<String, Object> record) throws SQLException {
        Object idObj = record.get("ID");
        if (idObj == null) {
            throw new SQLException("ID записи не указан в данных для обновления");
        }
        int id;
        if (idObj instanceof Integer) {
            id = (Integer) idObj;
        } else if (idObj instanceof String) {
            id = Integer.parseInt((String) idObj);
        } else {
            throw new SQLException("Неверный тип данных для ID: " + idObj.getClass().getName());
        }

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(entityName.toLowerCase()).append(" SET ");
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            if (!entry.getKey().equals("ID")) {
                sql.append(entry.getKey()).append(" = ?, ");
            }
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE ID = ?");
        System.out.println("Update SQL: " + sql.toString());
        System.out.println("Record: " + record);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                if (!entry.getKey().equals("ID")) {
                    stmt.setObject(paramIndex++, entry.getValue());
                }
            }
            stmt.setInt(paramIndex, id);
            stmt.executeUpdate();
        }
    }

    public void deleteRecord(String entityName, int id) throws SQLException {
        String sql = "DELETE FROM " + entityName.toLowerCase() + " WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Map<String, Object>> getAllRecords(String entityName) throws SQLException {
        List<Map<String, Object>> records = new ArrayList<>();
        String sql = "SELECT * FROM " + entityName.toLowerCase();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String columnName = rs.getMetaData().getColumnName(i).toUpperCase();
                    if (columnName.equals("ID")) {
                        record.put(columnName, rs.getInt(i));
                    } else {
                        record.put(columnName, rs.getObject(i));
                    }
                }
                records.add(record);
            }
        }
        return records;
    }

    public Map<String, Object> getRecordById(String entityName, int id) throws SQLException {
        Map<String, Object> record = new HashMap<>();
        String sql = "SELECT * FROM " + entityName.toLowerCase() + " WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        String columnName = rs.getMetaData().getColumnName(i).toUpperCase();
                        if (columnName.equals("ID")) {
                            record.put(columnName, rs.getInt(i));
                        } else {
                            record.put(columnName, rs.getObject(i));
                        }
                    }
                } else {
                    throw new SQLException("Запись с ID " + id + " не найдена в таблице " + entityName);
                }
            }
        }
        return record;
    }

    private String mapDataType(String dbType) {
        return switch (dbType.toLowerCase()) {
            case "integer", "serial" -> "INTEGER";
            case "numeric", "decimal" -> "DECIMAL";
            case "date" -> "DATE";
            default -> "TEXT";
        };
    }
}