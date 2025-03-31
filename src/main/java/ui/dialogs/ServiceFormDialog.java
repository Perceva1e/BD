package ui.dialogs;

import controller.ServiceController;
import model.Service;
import util.DurationParser;
import validation.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.util.regex.Pattern;

public class ServiceFormDialog extends JDialog {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9 \\-]{3,}$");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^[A-Za-z \\-]{3,}$");

    private final JTextField txtName = new JTextField(20);
    private final JTextField txtCategory = new JTextField(15);
    private final JTextField txtCost = new JTextField(10);
    private final JTextField txtDuration = new JTextField(15);
    private final ServiceController controller;
    private final Runnable onSuccess;
    private final Service editingService;
    private final InputValidator validator = new InputValidator();

    public ServiceFormDialog(Service service, ServiceController controller, Runnable onSuccess) {
        this.editingService = service;
        this.controller = controller;
        this.onSuccess = onSuccess;

        setTitle(editingService == null ? "New Service" : "Edit Service");
        initUI();
        setupWindow();
    }

    private void initUI() {
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        fieldsPanel.add(new JLabel("Name* (3+ chars):"));
        fieldsPanel.add(txtName);
        fieldsPanel.add(new JLabel("Category* (3+ chars):"));
        fieldsPanel.add(txtCategory);
        fieldsPanel.add(new JLabel("Cost* (>0):"));
        fieldsPanel.add(txtCost);
        fieldsPanel.add(new JLabel("Duration* (e.g. 2h30m):"));
        fieldsPanel.add(txtDuration);

        if (editingService != null) {
            txtName.setText(editingService.getName());
            txtCategory.setText(editingService.getCategory());
            txtCost.setText(String.valueOf(editingService.getCost()));
            txtDuration.setText(editingService.getFormattedDuration());
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveService());

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

    private void saveService() {
        try {
            StringBuilder errors = new StringBuilder();

            String name = txtName.getText().trim();
            if (!NAME_PATTERN.matcher(name).matches()) {
                errors.append("• Invalid name format! (3+ chars, a-z, 0-9, hyphens)\n");
            }

            String category = txtCategory.getText().trim();
            if (!CATEGORY_PATTERN.matcher(category).matches()) {
                errors.append("• Invalid category format! (3+ chars, a-z, hyphens)\n");
            }

            int cost = 0;
            try {
                cost = Integer.parseInt(txtCost.getText().trim());
                if (cost <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                errors.append("• Cost must be positive integer\n");
            }

            Duration duration = Duration.ZERO;
            try {
                duration = DurationParser.parse(txtDuration.getText().trim());
                if (duration.isZero()) {
                    errors.append("• Duration cannot be zero\n");
                }
            } catch (Exception e) {
                errors.append("• Invalid duration format\n");
            }

            if (errors.length() > 0) {
                showError("Validation errors:\n" + errors);
                return;
            }

            Service service = new Service();
            if (editingService != null) service.setId(editingService.getId());

            service.setName(name);
            service.setCategory(category);
            service.setCost(cost);
            service.setDuration(duration);

            if (editingService == null) {
                controller.addService(service);
            } else {
                controller.updateService(service);
            }

            onSuccess.run();
            dispose();
        } catch (Exception ex) {
            showError("Error saving service: " + ex.getMessage());
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