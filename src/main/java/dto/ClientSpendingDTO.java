package dto;

public class ClientSpendingDTO {
    private final int clientId;
    private final String fullName;
    private final int totalSpent;

    public ClientSpendingDTO(int clientId, String fullName, int totalSpent) {
        this.clientId = clientId;
        this.fullName = fullName;
        this.totalSpent = totalSpent;
    }

    @Override
    public String toString() {
        return String.format("Client #%d: %-20s | Total Spent: %d",
                clientId, fullName, totalSpent);
    }
}