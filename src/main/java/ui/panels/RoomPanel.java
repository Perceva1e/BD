package ui.panels;

import controller.RoomController;
import model.Room;
import ui.components.TablePanel;
import ui.dialogs.RoomFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class RoomPanel extends TablePanel {
    private final RoomController controller = new RoomController();

    public RoomPanel() {
        super("Rooms Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            refreshData();
        } catch (SQLException ex) {
            showError(ex.getMessage());
            table.setModel(new RoomTableModel(Collections.emptyList()));
        }
    }

    @Override
    protected void onAdd() {
        try {
            new RoomFormDialog(null, controller, this::safeRefresh);
        } catch (Exception ex) {
            showError("Error initializing form: " + ex.getMessage());
        }
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            try {
                int id = (int) table.getModel().getValueAt(row, 0);
                Room room = controller.getRoomById(id);
                new RoomFormDialog(room, controller, this::safeRefresh);
            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this room and all related bookings?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteRoom(id);
                    safeRefresh();
                } catch (SQLException ex) {
                    showError("Delete failed: " + ex.getMessage());
                }
            }
        }
    }

    private void refreshData() throws SQLException {
        List<Room> rooms = controller.getAllRooms();
        table.setModel(new RoomTableModel(rooms));
        table.repaint();
    }

    private void safeRefresh() {
        try {
            refreshData();
        } catch (SQLException ex) {
            showError("Failed to refresh data: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class RoomTableModel extends AbstractTableModel {
        private final List<Room> rooms;
        private final String[] columns = {"ID", "Area", "Cost", "Max Guests", "Amenities", "Type ID"};

        public RoomTableModel(List<Room> rooms) {
            this.rooms = rooms != null ? rooms : Collections.emptyList();
        }

        @Override public int getRowCount() { return rooms.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Room room = rooms.get(row);
            return switch (col) {
                case 0 -> room.getId();
                case 1 -> room.getArea() + " m²";
                case 2 -> "$" + room.getCost();
                case 3 -> room.getMaxGuests();
                case 4 -> room.getAmenities();
                case 5 -> room.getRoomTypeId();
                default -> null;
            };
        }
    }
}