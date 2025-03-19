package dto;

public class ServiceCostDTO {
    private final String category;
    private final int totalCost;

    public ServiceCostDTO(String category, int totalCost) {
        this.category = category;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return String.format("Category: %-15s | Total Cost: %d",
                category, totalCost);
    }
}