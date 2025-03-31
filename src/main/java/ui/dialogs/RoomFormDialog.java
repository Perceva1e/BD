package ui.dialogs;

import controller.RoomController;
import dao.RoomDAO;
import model.Room;
import validation.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RoomFormDialog extends JDialog {
    private static final Pattern AMENITIES_PATTERN = Pattern.compile("^[A-Za-z0-9 ,'-]{3,}$");
    private final JTextField txtArea = new JTextField(10);
    private final JTextField txtCost = new JTextField(10);
    private final JTextField txtMaxGuests = new JTextField(10);
    private final JTextField txtAmenities = new JTextField(20);
    private final JTextField txtRoomType = new JTextField(10);
    private final RoomController controller;
    private final Runnable onSuccess;
    private final InputValidator validator = new InputValidator();
    private final Room editingRoom;

    public RoomFormDialog(Room room, RoomController controller, Runnable onSuccess) {
        this.editingRoom = room;
        this.controller = controller;
        this.onSuccess = onSuccess;

        setTitle(editingRoom == null ? "New Room" : "Edit Room");
        initUI();
        setupWindow();
    }

    private void initUI() {
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fieldsPanel.add(new JLabel("Area (m²)*:"));
        fieldsPanel.add(txtArea);
        fieldsPanel.add(new JLabel("Nightly Cost ($)*:"));
        fieldsPanel.add(txtCost);
        fieldsPanel.add(new JLabel("Max Guests*:"));
        fieldsPanel.add(txtMaxGuests);
        fieldsPanel.add(new JLabel("Amenities*:"));
        fieldsPanel.add(txtAmenities);
        fieldsPanel.add(new JLabel("Room Type ID*:"));
        fieldsPanel.add(txtRoomType);

        if (editingRoom != null) {
            txtArea.setText(String.valueOf(editingRoom.getArea()));
            txtCost.setText(String.valueOf(editingRoom.getCost()));
            txtMaxGuests.setText(String.valueOf(editingRoom.getMaxGuests()));
            txtAmenities.setText(editingRoom.getAmenities());
            txtRoomType.setText(String.valueOf(editingRoom.getRoomTypeId()));
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveRoom());

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
    }

    private void setupWindow() {
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    private void saveRoom() {
        try {
            StringBuilder errors = new StringBuilder();

            int area = validatePositiveInt(txtArea.getText(), "Area", errors);
            int cost = validatePositiveInt(txtCost.getText(), "Cost", errors);
            int guests = validatePositiveInt(txtMaxGuests.getText(), "Max Guests", errors);
            String amenities = txtAmenities.getText().trim();
            int typeId = validatePositiveInt(txtRoomType.getText(), "Room Type ID", errors);

            if (!AMENITIES_PATTERN.matcher(amenities).matches()) {
                errors.append("• Invalid amenities format!\n");
            }

            if (!new RoomDAO().existsRoomType(typeId)) {
                errors.append("• Room type ID does not exist!\n");
            }

            if (errors.length() > 0) {
                showError("Validation errors:\n" + errors);
                return;
            }

            Room room = new Room();
            if (editingRoom != null) room.setId(editingRoom.getId());

            room.setArea(area);
            room.setCost(cost);
            room.setMaxGuests(guests);
            room.setAmenities(amenities);
            room.setRoomTypeId(typeId);

            if (editingRoom == null) {
                controller.addRoom(room);
            } else {
                controller.updateRoom(room);
            }

            onSuccess.run();
            dispose();
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private int validatePositiveInt(String input, String fieldName, StringBuilder errors) {
        try {
            int value = Integer.parseInt(input);
            if (value <= 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            errors.append("• ").append(fieldName).append(" must be positive integer\n");
            return -1;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}