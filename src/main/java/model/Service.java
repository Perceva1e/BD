package model;

import java.time.Duration;

public class Service {
    private int id;
    private String name;
    private String category;
    private int cost;
    private Duration duration;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }

    public String getFormattedDuration() {
        long days = duration.toDays();
        Duration remainder = duration.minusDays(days);
        long hours = remainder.toHours();
        remainder = remainder.minusHours(hours);
        long minutes = remainder.toMinutes();

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(days == 1 ? " day " : " days ");
        }
        if (hours > 0) {
            sb.append(hours).append(hours == 1 ? " hour " : " hours ");
        }
        if (minutes > 0 || sb.length() == 0) {
            sb.append(minutes).append(minutes == 1 ? " minute" : " minutes");
        }
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return String.format(
                "Service #%d: %s (Category: %s, Cost: %d, Duration: %s)",
                id, name, category, cost, getFormattedDuration()
        );
    }
}