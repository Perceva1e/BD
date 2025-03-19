package dto;

public class PaymentStatsDTO {
    private final String paymentMethod;
    private final String status;
    private final int count;

    public PaymentStatsDTO(String paymentMethod, String status, int count) {
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("Method: %-10s | Status: %-10s | Count: %d",
                paymentMethod, status, count);
    }
}
