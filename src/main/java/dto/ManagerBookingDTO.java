package dto;

import java.time.LocalDate;

public class ManagerBookingDTO {
    private final String managerName;
    private final int bookingId;
    private final LocalDate checkInDate;
    private final int totalCost;

    public ManagerBookingDTO(String managerName, int bookingId,
                             LocalDate checkInDate, int totalCost) {
        this.managerName = managerName;
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return String.format("Manager: %-20s | Booking #%-4d | Check-in: %s | Total: %d",
                managerName, bookingId, checkInDate, totalCost);
    }
}