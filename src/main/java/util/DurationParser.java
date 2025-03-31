package util;

import java.time.Duration;

public class DurationParser {
    public static Duration parse(String input) throws IllegalArgumentException {
        try {
            input = input.trim().toLowerCase()
                    .replaceAll("\\s+", "")
                    .replace("day", "d")
                    .replace("days", "d")
                    .replace("h", "H")
                    .replace("m", "M")
                    .replace("d", "D");

            long days = 0;
            if (input.contains("D")) {
                String[] parts = input.split("D");
                days = Long.parseLong(parts[0]);
                input = parts.length > 1 ? parts[1] : "";
            }

            if (!input.startsWith("PT")) input = "PT" + input;
            Duration duration = Duration.parse(input);
            return duration.plus(Duration.ofDays(days));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid duration format. Examples: 1h30m, 45m, 2h, 1day2h");
        }
    }
}