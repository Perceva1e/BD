package controller;

import model.RoomType;
import service.RoomTypeService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;

public class RoomTypeController {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private RoomTypeService roomTypeService = new RoomTypeService();
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

    public List<RoomType> getAllRoomTypes() throws SQLException {
        return roomTypeService.getAllRoomTypes();
    }

    public RoomType getRoomTypeById(int id) throws SQLException {
        return roomTypeService.getRoomTypeById(id);
    }

    public void addRoomType(RoomType roomType) throws SQLException {
        roomTypeService.createRoomType(roomType);
    }

    public void updateRoomType(RoomType roomType) throws SQLException {
        roomTypeService.updateRoomTypeEntity(roomType);
    }

    public void deleteRoomType(int id) throws SQLException {
        roomTypeService.deleteRoomTypeEntity(id);
    }
}