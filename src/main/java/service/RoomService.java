package service;

import dao.RoomDAO;
import model.Room;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;
public class RoomService {
    private final RoomDAO roomDAO;
    private final InputValidator inputValidator;
    public RoomService() {
        this.roomDAO = new RoomDAO();
        this.inputValidator = new InputValidator();
    }
    public void addRoom(Scanner scanner) {
        try {
            Room room = new Room();
            System.out.print("Enter area: ");
            String inputs = scanner.nextLine().trim();
            room.setArea(inputValidator.readPositiveIntInput(inputs,"area"));
            System.out.print("Enter nightly cost: ");
            inputs = scanner.nextLine().trim();
            room.setCost(inputValidator.readPositiveIntInput(inputs,"nightly cost"));
            System.out.print("Enter maximum guests: ");
            inputs = scanner.nextLine().trim();
            room.setMaxGuests(inputValidator.readPositiveIntInput(inputs,"maximum guests"));

            while (true) {
                System.out.print("Enter amenities: ");
                String amenities = scanner.nextLine().trim();
                if (!amenities.isEmpty() && amenities.matches("^[A-Za-z0-9 ,'-]{3,}$")) {
                    room.setAmenities(amenities);
                    break;
                }
                System.out.println("Invalid amenities! Use letters, numbers, spaces, commas, apostrophes, and hyphens (min 3 chars)");
            }

            System.out.print("Enter room type ID: ");
            inputs = scanner.nextLine().trim();
            int roomTypeId = inputValidator.readIntInput(inputs);
            while (!roomDAO.existsRoomType(roomTypeId)) {
                System.out.print("Room type ID does not exist. Enter valid ID: ");
                inputs = scanner.nextLine().trim();
                roomTypeId = inputValidator.readIntInput(inputs);
            }
            room.setRoomTypeId(roomTypeId);

            roomDAO.addRoom(room);
            System.out.println("Room added successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void listRooms() {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            if (rooms.isEmpty()) System.out.println("\nNo rooms!");
            else rooms.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void updateRoom(Scanner scanner) {
        try {
            System.out.print("Enter room ID to update: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            Room existingRoom = roomDAO.getRoomById(id);
            if (existingRoom == null) {
                System.out.println("Room not found!");
                return;
            }

            System.out.println("Current room details:");
            System.out.println(existingRoom);

            while (true) {
                System.out.print("New area (m?) (press Enter to keep current): ");
                String areaInput = scanner.nextLine().trim();
                if (areaInput.isEmpty()) break;
                try {
                    int area = inputValidator.readPositiveIntInput(areaInput, "area");
                    existingRoom.setArea(area);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            while (true) {
                System.out.print("New cost (press Enter to keep current): ");
                String costInput = scanner.nextLine().trim();
                if (costInput.isEmpty()) break;
                try {
                    int cost = inputValidator.readPositiveIntInput(costInput, "cost");
                    existingRoom.setCost(cost);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            while (true) {
                System.out.print("New maximum guests (press Enter to keep current): ");
                String guestsInput = scanner.nextLine().trim();
                if (guestsInput.isEmpty()) break;
                try {
                    int guests = inputValidator.readPositiveIntInput(guestsInput, "maximum guests");
                    existingRoom.setMaxGuests(guests);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            while (true) {
                System.out.print("New amenities (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;
                if (input.matches("^[A-Za-z0-9 ,'-]{3,}$")) {
                    existingRoom.setAmenities(input);
                    break;
                }
                System.out.println("Invalid amenities! Use letters, numbers, spaces, commas, apostrophes, and hyphens (min 3 chars)");
            }

            while (true) {
                System.out.print("New type ID (press Enter to keep current): ");
                String typeIdInput = scanner.nextLine().trim();
                if (typeIdInput.isEmpty()) break;
                try {
                    int newTypeId = inputValidator.readPositiveIntInput(typeIdInput, "type ID");
                    if (!roomDAO.existsRoomType(newTypeId)) {
                        System.out.println("Room type ID does not exist!");
                        continue;
                    }
                    existingRoom.setRoomTypeId(newTypeId);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            roomDAO.updateRoom(existingRoom);
            System.out.println("Room updated successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void deleteRoom(Scanner scanner) {
        try {
            System.out.print("Enter room ID to delete: ");
            int id = inputValidator.readIntInput(scanner.nextLine().trim());

            System.out.println("All room and room type will be deleted: ");
            System.out.print("Confirm deletion? (y/n): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("y")) {
                System.out.println("Deletion canceled");
                return;
            }

            if (roomDAO.deleteRoom(id)) {
                System.out.println("Room and its type (if last room) deleted!");
            }
        } catch (Exception e) {
            System.err.println("Error deleting room: " + e.getMessage());
        }
    }
}