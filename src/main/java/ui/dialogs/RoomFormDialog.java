package ui.dialogs;

import controller.RoomController;
import model.Room;
import javax.swing.*;
import java.awt.*;
import validation.InputValidator;

public class RoomFormDialog extends JDialog {
    private final JTextField txtArea = new JTextField(10);
    private final JTextField txtCost = new JTextField(10);
    private final JTextField txtMaxGuests = new JTextField(10);
    private final JTextField txtAmenities = new JTextField(20);
    private final JTextField txtRoomType = new JTextField(10);
    private final InputValidator validator = new InputValidator();

    public RoomFormDialog(Room room, RoomController controller, Runnable onSuccess) {
        setTitle(room == null ? "New Room" : "Edit Room");
        setLayout(new BorderLayout());
        setModal(true);

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fieldsPanel.add(new JLabel("Area (m²):"));
        fieldsPanel.add(txtArea);
        fieldsPanel.add(new JLabel("Nightly Cost:"));
        fieldsPanel.add(txtCost);
        fieldsPanel.add(new JLabel("Max Guests:"));
        fieldsPanel.add(txtMaxGuests);
        fieldsPanel.add(new JLabel("Amenities:"));
        fieldsPanel.add(txtAmenities);
        fieldsPanel.add(new JLabel("Room Type ID:"));
        fieldsPanel.add(txtRoomType);

        if (room != null) {
            txtArea.setText(String.valueOf(room.getArea()));
            txtCost.setText(String.valueOf(room.getCost()));
            txtMaxGuests.setText(String.valueOf(room.getMaxGuests()));
            txtAmenities.setText(room.getAmenities());
            txtRoomType.setText(String.valueOf(room.getRoomTypeId()));
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveRoom(room, controller, onSuccess));

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void saveRoom(Room room, RoomController controller, Runnable onSuccess) {
        try {
            Room newRoom = new Room();
            if (room != null) newRoom.setId(room.getId());

            newRoom.setArea(validator.readPositiveIntInput(txtArea.getText(), "Area"));
            newRoom.setCost(validator.readPositiveIntInput(txtCost.getText(), "Cost"));
            newRoom.setMaxGuests(validator.readPositiveIntInput(txtMaxGuests.getText(), "Max Guests"));
            newRoom.setAmenities(txtAmenities.getText().trim());
            newRoom.setRoomTypeId(validator.readPositiveIntInput(txtRoomType.getText(), "Room Type ID"));

            if (room == null) {
                controller.addRoom(newRoom);
            } else {
                controller.updateRoom(newRoom);
            }

            onSuccess.run();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}