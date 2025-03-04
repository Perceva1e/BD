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
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class BackUpService {

    public void createBackup(Scanner scanner) {
        System.out.print("Enter backup file path: ");
        String path = scanner.nextLine().trim();

        try {
            String pgDumpPath = "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_dump.exe";

            ProcessBuilder pb = new ProcessBuilder(
                    pgDumpPath,
                    "-U", "postgres",
                    "-d", "postgres",
                    "-f", path
            );
            pb.environment().put("PGPASSWORD", "Den/25362004");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

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
            String psqlPath = "C:\\Program Files\\PostgreSQL\\17\\bin\\psql.exe";
            ProcessBuilder pb = new ProcessBuilder(
                    psqlPath,
                    "-U", "postgres",
                    "-d", "test",
                    "-f", path
            );
            pb.environment().put("PGPASSWORD", "Den/25362004");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

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