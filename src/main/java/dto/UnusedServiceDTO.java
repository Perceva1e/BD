package dto;

public class UnusedServiceDTO {
    private final String name;
    private final String category;
    private final int cost;

    public UnusedServiceDTO(String name, String category, int cost) {
        this.name = name;
        this.category = category;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("Service: %-15s | Category: %-10s | Cost: %d",
                name, category, cost);
    }
}
