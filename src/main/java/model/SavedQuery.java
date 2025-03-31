package model;

import java.time.LocalDateTime;

public class SavedQuery {
    private int id;
    private String name;
    private String sql;
    private LocalDateTime createdAt;

    public SavedQuery() {}

    public SavedQuery(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return name + " (" + createdAt.toLocalDate() + ")";
    }
}