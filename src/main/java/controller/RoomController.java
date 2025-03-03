package controllers;

import services.RoomService;
import java.util.Scanner;
import validation.InputValidator;
public class RoomController {
    private final RoomService roomService;
    private final Scanner scanner;
    private final InputValidator inputValidator;

    public RoomController() {
        this.roomService = new RoomService();
        this.scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator();
    }

    public void handleRooms() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Room Management ===");
            System.out.println("1. Add Room");
            System.out.println("2. List Rooms");
            System.out.println("3. Update Room");
            System.out.println("4. Delete Room");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            String inputs = scanner.nextLine().trim();
            int choice = inputValidator.readIntInput(inputs);
            switch (choice) {
                case 1 -> roomService.addRoom(scanner);
                case 2 -> roomService.listRooms();
                case 3 -> roomService.updateRoom(scanner);
                case 4 -> roomService.deleteRoom(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
