package ui.panels;

import controller.BookingController;
import model.Booking;
import ui.components.TablePanel;
import ui.dialogs.BookingFormDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BookingPanel extends TablePanel implements Refreshable {
    private final BookingController controller = new BookingController();
    private Refreshable paymentPanel;

    public BookingPanel() {
        super("Bookings Management");
        initTableModel();
    }

    public void setPaymentPanel(Refreshable paymentPanel) {
        this.paymentPanel = paymentPanel;
    }

    private void initTableModel() {
        try {
            refreshDataInternal();
        } catch (SQLException ex) {
            showError(ex.getMessage());
            table.setModel(new BookingTableModel(Collections.emptyList()));
        }
    }

    @Override
    protected void onAdd() {
        new BookingFormDialog(null, controller, this::safeRefresh);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            try {
                int id = (int) table.getModel().getValueAt(row, 0);
                Booking booking = controller.getBookingById(id);
                new BookingFormDialog(booking, controller, this::safeRefresh);
            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            }
        } else {
            showError("Please select a booking to edit");
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this booking and all related payments?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteBooking(id);
                    safeRefresh();
                    if (paymentPanel != null) {
                        paymentPanel.refreshData();
                    }
                    JOptionPane.showMessageDialog(
                            this,
                            "Booking and related payments deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (SQLException ex) {
                    showError("Delete failed: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Deletion canceled",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            showError("Please select a booking to delete");
        }
    }

    @Override
    public void refreshData() {
        try {
            refreshDataInternal();
        } catch (SQLException ex) {
            showError("Failed to refresh: " + ex.getMessage());
        }
    }

    private void refreshDataInternal() throws SQLException {
        List<Booking> bookings = controller.getAllBookings();
        table.setModel(new BookingTableModel(bookings));
        table.repaint();
    }

    private void safeRefresh() {
        try {
            refreshDataInternal();
        } catch (SQLException ex) {
            showError("Failed to refresh: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class BookingTableModel extends AbstractTableModel {
        private final List<Booking> bookings;
        private final String[] columns = {"ID", "Client ID", "Employee ID", "Check-In", "Check-Out", "Cost", "Status"};

        public BookingTableModel(List<Booking> bookings) {
            this.bookings = bookings != null ? bookings : Collections.emptyList();
        }

        @Override
        public int getRowCount() {
            return bookings.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Booking booking = bookings.get(row);
            return switch (col) {
                case 0 -> booking.getId();
                case 1 -> booking.getClientId();
                case 2 -> booking.getEmployeeId();
                case 3 -> booking.getCheckInDate().toString();
                case 4 -> booking.getCheckOutDate().toString();
                case 5 -> "$" + booking.getTotalCost();
                case 6 -> booking.getStatus();
                default -> null;
            };
        }
    }
}