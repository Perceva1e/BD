package controllers;

import services.RoomTypeService;
import java.util.Scanner;
import validation.InputValidator;

public class RoomTypeController {
    private final RoomTypeService roomTypeService;
    private final Scanner scanner;
    private final InputValidator inputValidator;

    public RoomTypeController() {
        this.roomTypeService = new RoomTypeService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handleRoomTypes() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Room Type Management ===");
            System.out.println("1. Add Room Type");
            System.out.println("2. List Room Types");
            System.out.println("3. Update Room Type");
            System.out.println("4. Delete Room Type");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);
            switch (choice) {
                case 1 -> roomTypeService.addRoomType(scanner);
                case 2 -> roomTypeService.listRoomTypes();
                case 3 -> roomTypeService.updateRoomType(scanner);
                case 4 -> roomTypeService.deleteRoomType(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}