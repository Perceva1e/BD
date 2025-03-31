package ui.dialogs;

import controller.BookingController;
import dao.ClientDAO;
import dao.EmployeeDAO;
import model.Booking;
import validation.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class BookingFormDialog extends JDialog {
    private final JTextField txtClientId = new JTextField(10);
    private final JTextField txtEmployeeId = new JTextField(10);
    private final JTextField txtCheckIn = new JTextField(10);
    private final JTextField txtCheckOut = new JTextField(10);
    private final JTextField txtTotalCost = new JTextField(10);
    private final JComboBox<String> cmbStatus = new JComboBox<>(
            new String[]{"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"}
    );
    private final BookingController controller;
    private final Runnable onSuccess;
    private final InputValidator validator = new InputValidator();

    public BookingFormDialog(Booking booking, BookingController controller, Runnable onSuccess) {
        this.controller = controller;
        this.onSuccess = onSuccess;

        setTitle(booking == null ? "New Booking" : "Edit Booking");
        initUI(booking);
        setupWindow();
    }

    private void initUI(Booking booking) {
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        fieldsPanel.add(new JLabel("Client ID*:"));
        fieldsPanel.add(txtClientId);
        fieldsPanel.add(new JLabel("Employee ID*:"));
        fieldsPanel.add(txtEmployeeId);
        fieldsPanel.add(new JLabel("Check-In* (YYYY-MM-DD):"));
        fieldsPanel.add(txtCheckIn);
        fieldsPanel.add(new JLabel("Check-Out* (YYYY-MM-DD):"));
        fieldsPanel.add(txtCheckOut);
        fieldsPanel.add(new JLabel("Total Cost*:"));
        fieldsPanel.add(txtTotalCost);
        fieldsPanel.add(new JLabel("Status*:"));
        fieldsPanel.add(cmbStatus);

        if (booking != null) {
            txtClientId.setText(String.valueOf(booking.getClientId()));
            txtEmployeeId.setText(String.valueOf(booking.getEmployeeId()));
            txtCheckIn.setText(booking.getCheckInDate().toString());
            txtCheckOut.setText(booking.getCheckOutDate().toString());
            txtTotalCost.setText(String.valueOf(booking.getTotalCost()));
            cmbStatus.setSelectedItem(booking.getStatus());
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveBooking());

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

    private void saveBooking() {
        try {
            StringBuilder errors = new StringBuilder();
            LocalDate today = LocalDate.now();

            int clientId = 0;
            try {
                clientId = Integer.parseInt(txtClientId.getText().trim());
                if (!new ClientDAO().clientExists(clientId)) {
                    errors.append("• Client ID does not exist\n");
                }
            } catch (NumberFormatException | SQLException e) {
                errors.append("• Invalid Client ID format\n");
            }

            int employeeId = 0;
            try {
                employeeId = Integer.parseInt(txtEmployeeId.getText().trim());
                if (!new EmployeeDAO().employeeExists(employeeId)) {
                    errors.append("• Employee ID does not exist\n");
                }
            } catch (NumberFormatException | SQLException e) {
                errors.append("• Invalid Employee ID format\n");
            }

            LocalDate checkIn = null;
            LocalDate checkOut = null;
            try {
                checkIn = LocalDate.parse(txtCheckIn.getText().trim());
                if (checkIn.isBefore(today)) {
                    errors.append("• Check-in date cannot be in the past\n");
                }
            } catch (DateTimeParseException e) {
                errors.append("• Invalid check-in date format\n");
            }

            try {
                checkOut = LocalDate.parse(txtCheckOut.getText().trim());
                if (checkIn != null && checkOut.isBefore(checkIn)) {
                    errors.append("• Check-out must be after check-in\n");
                }
            } catch (DateTimeParseException e) {
                errors.append("• Invalid check-out date format\n");
            }

            int totalCost = 0;
            try {
                totalCost = Integer.parseInt(txtTotalCost.getText().trim());
                if (totalCost <= 0) errors.append("• Cost must be positive\n");
            } catch (NumberFormatException e) {
                errors.append("• Invalid cost format\n");
            }

            String status = (String) cmbStatus.getSelectedItem();
            if (!validator.isValidStatus(status)) {
                errors.append("• Invalid status selection\n");
            }

            if (errors.length() > 0) {
                showError("Validation errors:\n" + errors.toString());
                return;
            }

            Booking booking = new Booking();
            booking.setClientId(clientId);
            booking.setEmployeeId(employeeId);
            booking.setCheckInDate(checkIn);
            booking.setCheckOutDate(checkOut);
            booking.setTotalCost(totalCost);
            booking.setStatus(status);

            if (booking.getId() == 0) {
                controller.addBooking(booking);
            } else {
                controller.updateBooking(booking);
            }

            onSuccess.run();
            dispose();
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
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