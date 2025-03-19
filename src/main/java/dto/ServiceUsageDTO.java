package dto;

import java.time.Duration;
import java.time.LocalDate;

public class ServiceUsageDTO {
    private final String serviceName;
    private final int cost;
    private final Duration duration;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    public ServiceUsageDTO(String serviceName, int cost, Duration duration,
                           LocalDate checkInDate, LocalDate checkOutDate) {
        this.serviceName = serviceName;
        this.cost = cost;
        this.duration = duration;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    private String formatDuration() {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public String toString() {
        return String.format("Service: %-15s | Cost: %-5d | Duration: %-5s | Dates: %s to %s",
                serviceName, cost, formatDuration(), checkInDate, checkOutDate);
    }
}
