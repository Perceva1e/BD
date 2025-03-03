package validation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Scanner;

public class InputValidator {

    public InputValidator(){
        this.scanner = new Scanner(System.in);
    }
    private final Scanner scanner;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s-]{3,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+375\\d{9}$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern AMENITIES_PATTERN = Pattern.compile("^[A-Za-z0-9,\\s-]{3,}$");

    private static final Set<String> BOOKING_STATUSES = new HashSet<>(
            Arrays.asList("PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"));
    private static final Set<String> PAYMENT_TYPES = new HashSet<>(
            Arrays.asList("CREDIT CARD", "DEBIT CARD", "CASH", "BANK TRANSFER"));
    private static final Set<String> COMFORT_LEVELS = new HashSet<>(
            Arrays.asList("LOW", "MEDIUM", "HIGH", "VERY HIGH"));

    public boolean isValidStatus(String status) {
        return BOOKING_STATUSES.contains(status.toUpperCase());
    }

    public int readIntInput(String inputs) {
        while (true) {
            try {
                return Integer.parseInt(inputs);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
                Scanner scanner = new Scanner(System.in);
                inputs = scanner.nextLine().trim();
            }
        }
    }

    public int readPositiveIntInput(String inputs, String fieldName) {
        while (true) {
            System.out.printf("Enter %s: ", fieldName);
            int value = readIntInput(inputs);
            if (value > 0) return value;
            System.out.printf("%s must be positive. ", fieldName);
        }
    }

    public String readValidName(Scanner scanner, String fieldName) {
        String input;
        do {
            System.out.printf("Enter %s: ", fieldName);
            input = scanner.nextLine().trim();
            if (!NAME_PATTERN.matcher(input).matches()) {
                System.out.println("Invalid format! Minimum 3 characters, letters, spaces and hyphens only");
            }
        } while (!NAME_PATTERN.matcher(input).matches());
        return input;
    }

    public String readValidPhone(Scanner scanner) {
        String input;
        do {
            System.out.print("Enter phone (+375XXXXXXXXX): ");
            input = scanner.nextLine().trim();
            if (!PHONE_PATTERN.matcher(input).matches()) {
                System.out.println("Invalid format! Example: +375291234567");
            }
        } while (!PHONE_PATTERN.matcher(input).matches());
        return input;
    }

    public String readValidPassport(Scanner scanner) {
        String input;
        do {
            System.out.print("Enter passport number (10 digits): ");
            input = scanner.nextLine().trim();
            if (!PASSPORT_PATTERN.matcher(input).matches()) {
                System.out.println("Invalid format! Must be 10 digits");
            }
        } while (!PASSPORT_PATTERN.matcher(input).matches());
        return input;
    }

    public LocalDate readValidBirthDate(Scanner scanner) {
        while (true) {
            System.out.print("Enter birth date (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isAfter(LocalDate.now().minusYears(18))) {
                    System.out.println("Must be at least 18 years old!");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Use YYYY-MM-DD");
            }
        }
    }

    public LocalDate readFutureDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Date cannot be in the past!");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Use YYYY-MM-DD");
            }
        }
    }

    public String readValidStatus(Scanner scanner) {
        String input;
        do {
            System.out.print("Enter status: ");
            input = scanner.nextLine().trim().toUpperCase();
            if (!BOOKING_STATUSES.contains(input)) {
                System.out.println("Invalid status! Allowed values: " + String.join(", ", BOOKING_STATUSES));
            }
        } while (!BOOKING_STATUSES.contains(input));
        return input;
    }

    public Duration parseDurationInput(String input) {
        try {
            input = input.trim().toUpperCase()
                    .replace("DAYS", "D")
                    .replace("DAY", "D")
                    .replace("HOURS", "H")
                    .replace("HOUR", "H")
                    .replace("MINUTES", "M")
                    .replace("MINUTE", "M")
                    .replace(" ", "");

            long days = 0;
            if (input.contains("D")) {
                String[] parts = input.split("D");
                days = Long.parseLong(parts[0]);
                input = parts.length > 1 ? parts[1] : "";
            }

            if (!input.startsWith("PT")) input = "PT" + input;
            Duration duration = Duration.parse(input);
            return duration.plusDays(days);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid duration format. Examples: 1h30m, 45m, 2h, 1day2h");
        }
    }

    public String readValidPaymentType(Scanner scanner) {
        String input;
        do {
            System.out.print("Enter payment type: ");
            input = scanner.nextLine().trim().toUpperCase();
            if (!PAYMENT_TYPES.contains(input)) {
                System.out.println("Invalid type! Allowed values: " + String.join(", ", PAYMENT_TYPES));
            }
        } while (!PAYMENT_TYPES.contains(input));
        return input;
    }

    public String readValidComfortLevel(Scanner scanner) {
        String input;
        do {
            System.out.print("Enter comfort level: ");
            input = scanner.nextLine().trim().toUpperCase();
            if (!COMFORT_LEVELS.contains(input)) {
                System.out.println("Invalid level! Allowed values: " + String.join(", ", COMFORT_LEVELS));
            }
        } while (!COMFORT_LEVELS.contains(input));
        return input;
    }

    public String readValidAmenities(Scanner scanner) {
        String input;
        do {
            System.out.print("Enter amenities: ");
            input = scanner.nextLine().trim();
            if (!AMENITIES_PATTERN.matcher(input).matches()) {
                System.out.println("Invalid characters! Use letters, numbers, commas and hyphens");
            }
        } while (!AMENITIES_PATTERN.matcher(input).matches());
        return input;
    }

    public List<LocalDate> readValidSchedule(Scanner scanner) {
        List<LocalDate> schedule = new ArrayList<>();
        while (true) {
            System.out.print("Enter work dates (comma-separated, YYYY-MM-DD): ");
            String[] inputs = scanner.nextLine().split(",");
            boolean valid = true;

            for (String input : inputs) {
                try {
                    LocalDate date = LocalDate.parse(input.trim());
                    if (date.isBefore(LocalDate.now())) {
                        System.out.println("Date cannot be in the past: " + date);
                        valid = false;
                        break;
                    }
                    schedule.add(date);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format: " + input);
                    valid = false;
                    break;
                }
            }

            if (valid && !schedule.isEmpty()) {
                schedule.sort(LocalDate::compareTo);
                return schedule;
            }
            System.out.println("Please enter valid future dates in correct format");
            schedule.clear();
        }
    }
}