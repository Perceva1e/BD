package model;

import java.time.LocalDate;

public class Payment {
    private int id;
    private String paymentType;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String status;
    private int bookingId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    @Override
    public String toString() {
        return String.format(
                "Payment [ID=%d, Type=%s, Date=%s, Method=%s, Status=%s, BookingID=%d]",
                id, paymentType, paymentDate, paymentMethod, status, bookingId
        );
    }
}