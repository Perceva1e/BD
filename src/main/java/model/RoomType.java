package model;

public class RoomType {
    private int id;
    private String name;
    private String comfortLevel;
    private String category;
    private int costPerNight;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getComfortLevel() { return comfortLevel; }
    public void setComfortLevel(String comfortLevel) { this.comfortLevel = comfortLevel; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getCostPerNight() { return costPerNight; }
    public void setCostPerNight(int costPerNight) { this.costPerNight = costPerNight; }

    @Override
    public String toString() {
        return String.format(
                "RoomType [ID=%d, Name=%s, Comfort=%s, Category=%s, CostPerNight=%d]",
                id, name, comfortLevel, category, costPerNight
        );
    }
}