package services;

import dao.BookingDAO;
import dao.ClientDAO;
import dao.EmployeeDAO;
import validation.InputValidator;
import model.Booking;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class BackUpService {

    public void createBackup(Scanner scanner) {
        System.out.print("Enter backup file path: ");
        String path = scanner.nextLine();

        try {
            Process process = Runtime.getRuntime().exec(
                    "pg_dump -U postgres -d hotel -f " + path
            );
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Backup created successfully!");
            } else {
                System.err.println("Backup failed with code: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Backup error: " + e.getMessage());
        }
    }

    public void restoreBackup(Scanner scanner) {
        System.out.print("Enter backup file path: ");
        String path = scanner.nextLine();

        try {
            Process process = Runtime.getRuntime().exec(
                    "psql -U postgres -d hotel -f " + path
            );
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Backup restored successfully!");
            } else {
                System.err.println("Restore failed with code: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Restore error: " + e.getMessage());
        }
    }
}