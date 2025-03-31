package ui.dialogs;

import controller.RoomTypeController;
import model.RoomType;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;
import java.sql.SQLException;

public class RoomTypeFormDialog extends JDialog {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s-]{3,}$");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^[A-Za-z]{4,}$");

    private final JTextField txtName = new JTextField(20);
    private final JComboBox<String> cmbComfort = new JComboBox<>(new String[]{"Low", "Medium", "High", "Very High"});
    private final JTextField txtCategory = new JTextField(15);
    private final JTextField txtCost = new JTextField(10);
    private final RoomTypeController controller;
    private final Runnable onSuccess;
    private final RoomType editingType;

    public RoomTypeFormDialog(RoomType type, RoomTypeController controller, Runnable onSuccess) {
        this.editingType = type;
        this.controller = controller;
        this.onSuccess = onSuccess;

        setTitle(editingType == null ? "New Room Type" : "Edit Room Type");
        initUI();
        setupWindow();
    }

    private void initUI() {
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        fieldsPanel.add(new JLabel("Name (3+ letters, a-z only):"));
        fieldsPanel.add(txtName);
        fieldsPanel.add(new JLabel("Comfort Level:"));
        fieldsPanel.add(cmbComfort);
        fieldsPanel.add(new JLabel("Category (4+ letters):"));
        fieldsPanel.add(txtCategory);
        fieldsPanel.add(new JLabel("Cost/Night (> 0 $):"));
        fieldsPanel.add(txtCost);

        if (editingType != null) {
            txtName.setText(editingType.getName());
            cmbComfort.setSelectedItem(editingType.getComfortLevel());
            txtCategory.setText(editingType.getCategory());
            txtCost.setText(String.valueOf(editingType.getCostPerNight()));
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveType());

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

    private void saveType() {
        try {
            StringBuilder errors = new StringBuilder();

            String name = txtName.getText().trim();
            String category = txtCategory.getText().trim();
            String costStr = txtCost.getText().trim();

            if (!NAME_PATTERN.matcher(name).matches()) {
                errors.append("• Invalid name format!\n");
            }

            if (!CATEGORY_PATTERN.matcher(category).matches()) {
                errors.append("• Invalid category format!\n");
            }

            int cost = 0;
            try {
                cost = Integer.parseInt(costStr);
                if (cost <= 0) errors.append("• Cost must be positive!\n");
            } catch (NumberFormatException e) {
                errors.append("• Invalid cost format!\n");
            }

            if (errors.length() > 0) {
                showErrorDialog(errors.toString());
                return;
            }

            RoomType type = new RoomType();
            if (editingType != null) type.setId(editingType.getId());

            type.setName(name);
            type.setComfortLevel((String) cmbComfort.getSelectedItem());
            type.setCategory(category);
            type.setCostPerNight(cost);

            if (editingType == null) {
                controller.addRoomType(type);
            } else {
                controller.updateRoomType(type);
            }

            onSuccess.run();
            dispose();
        } catch (SQLException ex) {
            showErrorDialog("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}