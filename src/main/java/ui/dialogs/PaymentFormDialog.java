package ui.dialogs;

import controller.PaymentController;
import model.Payment;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class PaymentFormDialog extends JDialog {
    private static final Pattern ID_PATTERN = Pattern.compile("^\\d+$");

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

    private final PaymentController controller;
    private final Runnable onSuccess;
    private final Payment editingPayment;

    public PaymentFormDialog(Payment payment, PaymentController controller, Runnable onSuccess) {
        this.editingPayment = payment;
        this.controller = controller;
        this.onSuccess = onSuccess;

        setTitle(editingPayment == null ? "New Payment" : "Edit Payment");
        initializeUI();
        setupWindow();
    }

    private void initializeUI() {
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        addFormFields(fieldsPanel);
        populateFields();

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> savePayment());

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
    }

    private void addFormFields(JPanel panel) {
        panel.add(new JLabel("Type*:"));
        panel.add(cmbType);
        panel.add(new JLabel("Date* (YYYY-MM-DD):"));
        panel.add(txtDate);
        panel.add(new JLabel("Method*:"));
        panel.add(cmbMethod);
        panel.add(new JLabel("Status*:"));
        panel.add(cmbStatus);
        panel.add(new JLabel("Booking ID*:"));
        panel.add(txtBookingId);
    }

    private void populateFields() {
        if (editingPayment != null) {
            cmbType.setSelectedItem(editingPayment.getPaymentType());
            txtDate.setText(editingPayment.getPaymentDate().toString());
            cmbMethod.setSelectedItem(editingPayment.getPaymentMethod());
            cmbStatus.setSelectedItem(editingPayment.getStatus());
            txtBookingId.setText(String.valueOf(editingPayment.getBookingId()));
        }
    }

    private void setupWindow() {
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    private void savePayment() {
        try {
            StringBuilder errors = new StringBuilder();

            LocalDate paymentDate = null;
            try {
                paymentDate = LocalDate.parse(txtDate.getText().trim());
                if (paymentDate.isBefore(LocalDate.now())) {
                    errors.append("• Date cannot be in the past\n");
                }
            } catch (DateTimeParseException e) {
                errors.append("• Invalid date format\n");
            }

            int bookingId = 0;
            if (!ID_PATTERN.matcher(txtBookingId.getText().trim()).matches()) {
                errors.append("• Invalid Booking ID format\n");
            } else {
                try {
                    bookingId = Integer.parseInt(txtBookingId.getText().trim());
                    if (!controller.isValidBookingId(bookingId)) {
                        errors.append("• Booking ID does not exist\n");
                    }
                } catch (SQLException ex) {
                    errors.append("• Error verifying booking\n");
                }
            }

            if (errors.length() > 0) {
                showError("Validation errors:\n" + errors);
                return;
            }

            Payment payment = new Payment();
            if (editingPayment != null) payment.setId(editingPayment.getId());

            payment.setPaymentType((String) cmbType.getSelectedItem());
            payment.setPaymentDate(paymentDate);
            payment.setPaymentMethod((String) cmbMethod.getSelectedItem());
            payment.setStatus((String) cmbStatus.getSelectedItem());
            payment.setBookingId(bookingId);

            if (editingPayment == null) {
                controller.addPayment(payment);
            } else {
                controller.updatePayment(payment);
            }

            onSuccess.run();
            dispose();
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
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