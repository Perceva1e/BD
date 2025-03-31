package ui.dialogs;

import controller.RoomTypeController;
import model.RoomType;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class RoomTypeFormDialog extends JDialog {
    private final JTextField txtName = new JTextField(20);
    private final JComboBox<String> cmbComfort = new JComboBox<>(
            new String[]{"Low", "Medium", "High", "Very High"}
    );
    private final JTextField txtCategory = new JTextField(15);
    private final JTextField txtCost = new JTextField(10);

    public RoomTypeFormDialog(RoomType type, RoomTypeController controller, Runnable onSuccess) {
        setTitle(type == null ? "New Room Type" : "Edit Room Type");
        setLayout(new BorderLayout());
        setModal(true);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(txtName);
        fieldsPanel.add(new JLabel("Comfort Level:"));
        fieldsPanel.add(cmbComfort);
        fieldsPanel.add(new JLabel("Category:"));
        fieldsPanel.add(txtCategory);
        fieldsPanel.add(new JLabel("Cost/Night:"));
        fieldsPanel.add(txtCost);

        if (type != null) {
            txtName.setText(type.getName());
            cmbComfort.setSelectedItem(type.getComfortLevel());
            txtCategory.setText(type.getCategory());
            txtCost.setText(String.valueOf(type.getCostPerNight()));
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveType(type, controller, onSuccess));

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void saveType(RoomType type, RoomTypeController controller, Runnable onSuccess) {
        try {
            RoomType newType = new RoomType();
            if (type != null) newType.setId(type.getId());

            newType.setName(txtName.getText().trim());
            newType.setComfortLevel((String) cmbComfort.getSelectedItem());
            newType.setCategory(txtCategory.getText().trim());
            newType.setCostPerNight(Integer.parseInt(txtCost.getText()));

            if (type == null) {
                controller.addRoomType(newType);
            } else {
                controller.updateRoomType(newType);
            }

            onSuccess.run();
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid cost format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}