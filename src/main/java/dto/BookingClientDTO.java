package dto;

import java.time.LocalDate;

public class BookingClientDTO {
    private int bookingId;
    private LocalDate checkInDate;
    private String clientName;

    public BookingClientDTO(int bookingId, LocalDate checkInDate, String clientName) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return String.format("Booking #%d | Check-in: %s | Client: %s",
                bookingId, checkInDate, clientName);
    }
}