package dto;

import java.time.LocalDate;

public class PaymentBookingDTO {
    private int paymentId;
    private LocalDate paymentDate;
    private int totalCost;
    private String bookingStatus;

    public PaymentBookingDTO(int paymentId, LocalDate paymentDate, int totalCost, String bookingStatus) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.totalCost = totalCost;
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return String.format("Payment #%d | Date: %s | Amount: %d | Booking Status: %s",
                paymentId, paymentDate, totalCost, bookingStatus);
    }
}