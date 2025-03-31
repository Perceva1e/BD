package ui.dialogs;

import controller.BookingController;
import model.Booking;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BookingFormDialog extends JDialog {
    private final JTextField txtClientId = new JTextField(10);
    private final JTextField txtCheckIn = new JTextField(10);
    private final JTextField txtCheckOut = new JTextField(10);
    private final JComboBox<String> cmbStatus = new JComboBox<>(
            new String[]{"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"}
    );

    public BookingFormDialog(Booking booking, BookingController controller, Runnable onSuccess) {
        setTitle(booking == null ? "New Booking" : "Edit Booking");
        setLayout(new BorderLayout());
        setModal(true);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.add(new JLabel("Client ID:"));
        fieldsPanel.add(txtClientId);
        fieldsPanel.add(new JLabel("Check-In (YYYY-MM-DD):"));
        fieldsPanel.add(txtCheckIn);
        fieldsPanel.add(new JLabel("Check-Out (YYYY-MM-DD):"));
        fieldsPanel.add(txtCheckOut);
        fieldsPanel.add(new JLabel("Status:"));
        fieldsPanel.add(cmbStatus);

        if (booking != null) {
            txtClientId.setText(String.valueOf(booking.getClientId()));
            txtCheckIn.setText(booking.getCheckInDate().toString());
            txtCheckOut.setText(booking.getCheckOutDate().toString());
            cmbStatus.setSelectedItem(booking.getStatus());
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveBooking(booking, controller, onSuccess));

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveBooking(Booking booking, BookingController controller, Runnable onSuccess) {
        try {
            Booking newBooking = new Booking();
            if (booking != null) newBooking.setId(booking.getId());

            newBooking.setClientId(Integer.parseInt(txtClientId.getText()));
            newBooking.setCheckInDate(LocalDate.parse(txtCheckIn.getText()));
            newBooking.setCheckOutDate(LocalDate.parse(txtCheckOut.getText()));
            newBooking.setStatus((String) cmbStatus.getSelectedItem());

            if (booking == null) {
                controller.addBooking(newBooking);
            } else {
                controller.updateBooking(newBooking);
            }

            onSuccess.run();
            dispose();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Client ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}