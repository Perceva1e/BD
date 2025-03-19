package dto;

public class RoomTypeCostDTO {
    private final String category;
    private final int minCost;
    private final int maxCost;

    public RoomTypeCostDTO(String category, int minCost, int maxCost) {
        this.category = category;
        this.minCost = minCost;
        this.maxCost = maxCost;
    }

    @Override
    public String toString() {
        return String.format("Category: %-15s | Min Cost: %-6d | Max Cost: %d",
                category, minCost, maxCost);
    }
}