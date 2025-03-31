package ui.dialogs;

import controller.ClientController;
import model.Client;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class ClientFormDialog extends JDialog {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s-]{3,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+375\\d{9}$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^\\d{10}$");

    private final JTextField txtName = new JTextField(20);
    private final JTextField txtPhone = new JTextField(15);
    private final JTextField txtPassport = new JTextField(20);
    private final JTextField txtBirthDate = new JTextField(10);
    private final ClientController controller;
    private final Runnable onSuccess;
    private final Client editingClient;

    public ClientFormDialog(Client client, ClientController controller, Runnable onSuccess) {
        super((JFrame) null, true);
        this.editingClient = client;
        this.controller = controller;
        this.onSuccess = onSuccess;

        setTitle(editingClient == null ? "New Client" : "Edit Client");
        initUI(editingClient);
        setupWindow();
    }

    private void initUI(Client client) {
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        fieldsPanel.add(new JLabel("Full Name (3+ letters):"));
        fieldsPanel.add(txtName);
        fieldsPanel.add(new JLabel("Phone (+375XXXXXXXXX):"));
        fieldsPanel.add(txtPhone);
        fieldsPanel.add(new JLabel("Passport (10 digits):"));
        fieldsPanel.add(txtPassport);
        fieldsPanel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        fieldsPanel.add(txtBirthDate);

        if (client != null) {
            txtName.setText(client.getFullName());
            txtPhone.setText(client.getPhone());
            txtPassport.setText(client.getPassportNumber());
            txtBirthDate.setText(client.getBirthDate().toString());
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveClient());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(btnSave, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private void setupWindow() {
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void saveClient() {
        try {
            StringBuilder errors = new StringBuilder();

            String name = txtName.getText().trim();
            if (!NAME_PATTERN.matcher(name).matches()) {
                errors.append("- Invalid name! 3+ letters, spaces/hyphens only\n");
            }

            String phone = txtPhone.getText().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                errors.append("- Invalid phone! Format: +375XXXXXXXXX\n");
            }

            String passport = txtPassport.getText().trim();
            if (!PASSPORT_PATTERN.matcher(passport).matches()) {
                errors.append("- Invalid passport! 10 digits required\n");
            }

            LocalDate birthDate = null;
            try {
                birthDate = LocalDate.parse(txtBirthDate.getText().trim());
                if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
                    errors.append("- Client must be at least 18 years old\n");
                }
            } catch (Exception e) {
                errors.append("- Invalid date format! Use YYYY-MM-DD\n");
            }

            if (errors.length() > 0) {
                JOptionPane.showMessageDialog(this,
                        "Please correct the following errors:\n" + errors,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client client = new Client();
            if (editingClient != null) {
                client.setId(editingClient.getId());
            }
            client.setFullName(name);
            client.setPhone(phone);
            client.setPassportNumber(passport);
            client.setBirthDate(birthDate);

            if (client.getId() == 0) {
                controller.addClient(client);
            } else {
                controller.updateClient(client);
            }

            onSuccess.run();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving client: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}