package ui.dialogs;

import controller.PaymentController;
import model.Payment;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class PaymentFormDialog extends JDialog {
    private final JComboBox<String> cmbType = new JComboBox<>(
            new String[]{"Credit Card", "Debit Card", "Cash", "Bank Transfer"}
    );
    private final JTextField txtDate = new JTextField(10);
    private final JComboBox<String> cmbMethod = new JComboBox<>(
            new String[]{"Online", "In Person", "ATM"}
    );
    private final JComboBox<String> cmbStatus = new JComboBox<>(
            new String[]{"Pending", "Completed", "Failed", "Refunded"}
    );
    private final JTextField txtBookingId = new JTextField(10);

    public PaymentFormDialog(Payment payment, PaymentController controller, Runnable onSuccess) {
        setTitle(payment == null ? "New Payment" : "Edit Payment");
        setLayout(new BorderLayout());
        setModal(true);

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fieldsPanel.add(new JLabel("Type:"));
        fieldsPanel.add(cmbType);
        fieldsPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        fieldsPanel.add(txtDate);
        fieldsPanel.add(new JLabel("Method:"));
        fieldsPanel.add(cmbMethod);
        fieldsPanel.add(new JLabel("Status:"));
        fieldsPanel.add(cmbStatus);
        fieldsPanel.add(new JLabel("Booking ID:"));
        fieldsPanel.add(txtBookingId);

        if (payment != null) {
            cmbType.setSelectedItem(payment.getPaymentType());
            txtDate.setText(payment.getPaymentDate().toString());
            cmbMethod.setSelectedItem(payment.getPaymentMethod());
            cmbStatus.setSelectedItem(payment.getStatus());
            txtBookingId.setText(String.valueOf(payment.getBookingId()));
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> savePayment(payment, controller, onSuccess));

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void savePayment(Payment payment, PaymentController controller, Runnable onSuccess) {
        try {
            Payment newPayment = new Payment();
            if (payment != null) newPayment.setId(payment.getId());

            newPayment.setPaymentType((String) cmbType.getSelectedItem());
            newPayment.setPaymentDate(LocalDate.parse(txtDate.getText()));
            newPayment.setPaymentMethod((String) cmbMethod.getSelectedItem());
            newPayment.setStatus((String) cmbStatus.getSelectedItem());
            newPayment.setBookingId(Integer.parseInt(txtBookingId.getText()));

            if (payment == null) {
                controller.addPayment(newPayment);
            } else {
                controller.updatePayment(newPayment);
            }

            onSuccess.run();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}