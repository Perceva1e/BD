package ui.dialogs;

import controller.ClientController;
import model.Client;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ClientFormDialog extends JDialog {
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtPhone = new JTextField(15);
    private final JTextField txtPassport = new JTextField(20);
    private final JTextField txtBirthDate = new JTextField(10);

    public  ClientFormDialog(Client client, ClientController controller, Runnable onSuccess) {
        setTitle(client == null ? "New Client" : "Edit Client");
        setModal(true);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.add(new JLabel("Full Name:"));
        fieldsPanel.add(txtName);
        fieldsPanel.add(new JLabel("Phone:"));
        fieldsPanel.add(txtPhone);
        fieldsPanel.add(new JLabel("Passport:"));
        fieldsPanel.add(txtPassport);
        fieldsPanel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        fieldsPanel.add(txtBirthDate);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveClient(controller, onSuccess));

        if (client != null) {
            txtName.setText(client.getFullName());
            txtPhone.setText(client.getPhone());
            txtPassport.setText(client.getPassportNumber());
            txtBirthDate.setText(client.getBirthDate().toString());
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(btnSave, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void saveClient(ClientController controller, Runnable onSuccess) {
        try {
            Client client = new Client();
            client.setFullName(txtName.getText());
            client.setPhone(txtPhone.getText());
            client.setPassportNumber(txtPassport.getText());
            client.setBirthDate(LocalDate.parse(txtBirthDate.getText()));

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