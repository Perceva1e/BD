package services;

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
            String inputs = scanner.nextLine().trim();
            roomType.setCostPerNight(inputValidator.readPositiveIntInput(inputs,"cost"));

            roomTypeDAO.addRoomType(roomType);
            System.out.println("Room type added successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    public void listRoomTypes() {
        try {
            List<RoomType> roomType = roomTypeDAO.getAllRoomTypes();
            if (roomType.isEmpty()) System.out.println("\nNo roomType!");
            else roomType.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
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

            System.out.print("New cost (press Enter to keep current): ");
            inputs = scanner.nextLine().trim();
            if (!inputs.isEmpty()) {
                existingType.setCostPerNight(inputValidator.readPositiveIntInput(inputs, "cost"));
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
            String inputs = scanner.nextLine().trim();
            int id = inputValidator.readIntInput(inputs);

            if (roomTypeDAO.getRoomTypeById(id) == null) {
                System.out.println("Room type not found!");
                return;
            }

            if (roomDAO.hasRoomsWithType(id)) {
                System.out.println("Cannot delete: There are rooms using this type!");
                return;
            }

            if (roomTypeDAO.deleteRoomType(id)) {
                System.out.println("Room type deleted successfully!");
            } else {
                System.out.println("Failed to delete room type!");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}