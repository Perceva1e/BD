package service;

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

    private void executeBackupCommand(String path) {
        try {
            String pgDumpPath = "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_dump.exe";
            ProcessBuilder pb = new ProcessBuilder(
                    pgDumpPath,
                    "-U", "postgres",
                    "-d", "postgres",
                    "-f", path
            );
            pb.environment().put("PGPASSWORD", "Den/25362004");
            executeCommand(pb, "Backup");
        } catch (Exception e) {
            handleError("Backup", e);
        }
    }

    private void executeRestoreCommand(String path) {
        try {
            String psqlPath = "C:\\Program Files\\PostgreSQL\\17\\bin\\psql.exe";
            ProcessBuilder pb = new ProcessBuilder(
                    psqlPath,
                    "-U", "postgres",
                    "-d", "test",
                    "-f", path
            );
            pb.environment().put("PGPASSWORD", "Den/25362004");
            executeCommand(pb, "Restore");
        } catch (Exception e) {
            handleError("Restore", e);
        }
    }

    private void executeCommand(ProcessBuilder pb, String operation) throws Exception {
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println(operation + " completed successfully!");
        } else {
            System.err.println(operation + " failed with code: " + exitCode);
        }
    }

    private void handleError(String operation, Exception e) {
        System.err.println(operation + " error: " + e.getMessage());
        if (e.getCause() != null) {
            e.getCause().printStackTrace();
        }
    }
    public void createBackup(String path) {
        executeBackupCommand(path);
    }

    public void restoreBackup(String path) {
        executeRestoreCommand(path);
    }
    public void createBackup(Scanner scanner) {
        System.out.print("Enter backup file path: ");
        String path = scanner.nextLine();
        executeBackupCommand(path);
    }
    public void restoreBackup(Scanner scanner) {
        System.out.print("Enter backup file path: ");
        String path = scanner.nextLine();
        executeRestoreCommand(path);
    }
}