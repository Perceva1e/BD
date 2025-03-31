package service;

import dao.RoomTypeDAO;
import dao.RoomDAO;
import model.RoomType;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import validation.InputValidator;
import java.util.Set;
public class RoomTypeService {
    private final RoomTypeDAO roomTypeDAO;
    private final RoomDAO roomDAO;
    private final InputValidator inputValidator;
    public RoomTypeService() {
        this.roomTypeDAO = new RoomTypeDAO();
        this.inputValidator = new InputValidator();
        this.roomDAO = new RoomDAO();
    }
    public void addRoomType(Scanner scanner) {
        try {
            RoomType roomType = new RoomType();

            while(true) {
                System.out.print("Enter type name: ");
                String name = scanner.nextLine().trim();
                if (!name.isEmpty() && name.matches("^[A-Za-z\\s-]{3,}$")) {
                    roomType.setName(name);
                    break;
                }
                System.out.println("Invalid name! Use only letters, spaces and hyphens (min 3 chars)");
            }

            while(true) {
                System.out.print("Enter comfort level (Low/Medium/High/Very High): ");
                String comfort = scanner.nextLine().trim();
                if (Set.of("Low", "Medium", "High", "Very High").contains(comfort)) {
                    roomType.setComfortLevel(comfort);
                    break;
                }
                System.out.println("Invalid comfort level! Allowed values: Low, Medium, High, Very High");
            }

            while(true) {
                System.out.print("Enter category (Single/Double/Triple/Quadruple/Other): ");
                String category = scanner.nextLine().trim();
                if (!category.isEmpty() && category.matches("^[A-Za-z]{4,}$")) {
                    roomType.setCategory(category);
                    break;
                }
                System.out.println("Invalid category! Minimum 4 letters");
            }

            System.out.print("Enter cost per night: ");
            String inputs = scanner.nextLine().trim();
            roomType.setCostPerNight(inputValidator.readPositiveIntInput(inputs,"cost per night"));

            roomTypeDAO.addRoomType(roomType);
            System.out.println("Room type added successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    public List<RoomType> listRoomTypes() {
        List<RoomType> roomType = null;
        try {
            roomType = roomTypeDAO.getAllRoomTypes();
            if (roomType.isEmpty()) System.out.println("\nNo roomType!");
            else roomType.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return roomType;
    }

    public void updateRoomType(Scanner scanner) {
        try {
            System.out.print("Enter room type ID to update: ");
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            RoomType existingType = roomTypeDAO.getRoomTypeById(id);
            if (existingType == null) {
                System.out.println("Room type not found!");
                return;
            }

            System.out.println("Current room type details:");
            System.out.println(existingType);

            while(true) {
                System.out.print("New name (press Enter to keep current):");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;

                if (input.matches("^[A-Za-z\\s-]{3,}$")) {
                    existingType.setName(input);
                    break;
                }
                System.out.println("Invalid name! Use only letters, spaces and hyphens (min 3 chars)");
            }


            while(true) {
                System.out.print("New comfort level (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;

                if (Set.of("Low", "Medium", "High", "Very High").contains(input)) {
                    existingType.setComfortLevel(input);
                    break;
                }
                System.out.println("Invalid comfort level! Allowed values: Low, Medium, High, Very High");
            }

            while(true) {
                System.out.print("New category (press Enter to keep current): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;

                if (input.matches("^[A-Za-z]{4,}$")) {
                    existingType.setCategory(input);
                    break;
                }
                System.out.println("Invalid category! Minimum 4 letters");
            }

            System.out.print("New cost per night (press Enter to keep current): ");
            inputs = scanner.nextLine().trim();
            if (!inputs.isEmpty()) {
                existingType.setCostPerNight(inputValidator.readPositiveIntInput(inputs, "cost per night"));
            }

            roomTypeDAO.updateRoomType(existingType);
            System.out.println("Room type updated successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void deleteRoomType(Scanner scanner) {
        try {
            System.out.print("Enter room type ID to delete: ");
            int id = inputValidator.readIntInput(scanner.nextLine().trim());

            System.out.println("All room and room type will be deleted: ");
            System.out.print("Confirm deletion? (y/n): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("y")) {
                System.out.println("Deletion canceled");
                return;
            }
            if (roomTypeDAO.deleteRoomType(id)) {
                System.out.println("Room type and all related rooms deleted!");
            }
        } catch (Exception e) {
            System.err.println("Error deleting room type: " + e.getMessage());
        }
    }
    public List<RoomType> getAllRoomTypes() throws SQLException {
        return roomTypeDAO.getAllRoomTypes();
    }

    public RoomType getRoomTypeById(int id) throws SQLException {
        return roomTypeDAO.getRoomTypeById(id);
    }

    public void createRoomType(RoomType roomType) throws SQLException {
        roomTypeDAO.addRoomType(roomType);
    }

    public void updateRoomTypeEntity(RoomType roomType) throws SQLException {
        roomTypeDAO.updateRoomType(roomType);
    }

    public void deleteRoomTypeEntity(int id) throws SQLException {
        roomTypeDAO.deleteRoomType(id);
    }
}