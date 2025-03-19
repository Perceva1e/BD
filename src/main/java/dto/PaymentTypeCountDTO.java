package dto;

public class PaymentTypeCountDTO {
    private final String paymentType;
    private final int count;

    public PaymentTypeCountDTO(String paymentType, int count) {
        this.paymentType = paymentType;
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("Type: %-15s | Count: %d", paymentType, count);
    }
}