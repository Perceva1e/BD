package ui.dialogs;

import controller.ServiceController;
import model.Service;
import util.DurationParser;
import javax.swing.*;
import java.awt.*;
import validation.InputValidator;

public class ServiceFormDialog extends JDialog {
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtCategory = new JTextField(15);
    private final JTextField txtCost = new JTextField(10);
    private final JTextField txtDuration = new JTextField(15);
    private final InputValidator validator = new InputValidator();

    public ServiceFormDialog(Service service, ServiceController controller, Runnable onSuccess) {
        setTitle(service == null ? "New Service" : "Edit Service");
        setLayout(new BorderLayout());
        setModal(true);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(txtName);
        fieldsPanel.add(new JLabel("Category:"));
        fieldsPanel.add(txtCategory);
        fieldsPanel.add(new JLabel("Cost:"));
        fieldsPanel.add(txtCost);
        fieldsPanel.add(new JLabel("Duration (e.g. 2h30m):"));
        fieldsPanel.add(txtDuration);

        if (service != null) {
            txtName.setText(service.getName());
            txtCategory.setText(service.getCategory());
            txtCost.setText(String.valueOf(service.getCost()));
            txtDuration.setText(service.getFormattedDuration());
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveService(service, controller, onSuccess));

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void saveService(Service service, ServiceController controller, Runnable onSuccess) {
        try {
            Service newService = new Service();
            if (service != null) newService.setId(service.getId());

            newService.setName(txtName.getText().trim());
            newService.setCategory(txtCategory.getText().trim());
            newService.setCost(validator.readPositiveIntInput(txtCost.getText(), "Cost"));
            newService.setDuration(DurationParser.parse(txtDuration.getText()));

            if (service == null) {
                controller.addService(newService);
            } else {
                controller.updateService(newService);
            }

            onSuccess.run();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}