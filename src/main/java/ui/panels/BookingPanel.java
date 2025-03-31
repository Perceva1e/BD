package ui.panels;

import controller.BookingController;
import model.Booking;
import ui.components.TablePanel;
import ui.dialogs.BookingFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class BookingPanel extends TablePanel {
    private final BookingController controller = new BookingController();

    public BookingPanel() {
        super("Bookings Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            List<Booking> bookings = controller.getAllBookings();
            table.setModel(new BookingTableModel(bookings));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void onAdd() {
        new BookingFormDialog(null, controller, this::initTableModel);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);
            try {
                Booking booking = controller.getBookingById(id);
                new BookingFormDialog(booking, controller, this::initTableModel);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);
            try {
                controller.deleteBooking(id);
                initTableModel();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class BookingTableModel extends AbstractTableModel {
        private final List<Booking> bookings;
        private final String[] columns = {"ID", "Client ID", "Check-In", "Check-Out", "Status"};

        public BookingTableModel(List<Booking> bookings) {
            this.bookings = bookings;
        }

        @Override public int getRowCount() { return bookings.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Booking booking = bookings.get(row);
            return switch (col) {
                case 0 -> booking.getId();
                case 1 -> booking.getClientId();
                case 2 -> booking.getCheckInDate().toString();
                case 3 -> booking.getCheckOutDate().toString();
                case 4 -> booking.getStatus();
                default -> null;
            };
        }
    }
}