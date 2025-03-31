package ui.panels;

import controller.RoomController;
import model.Room;
import ui.components.TablePanel;
import ui.dialogs.RoomFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class RoomPanel extends TablePanel {
    private final RoomController controller = new RoomController();

    public RoomPanel() {
        super("Rooms Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            List<Room> rooms = controller.getAllRooms();
            table.setModel(new RoomTableModel(rooms));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void onAdd() {
        new RoomFormDialog(null, controller, this::initTableModel);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);
            try {
                Room room = controller.getRoomById(id);
                new RoomFormDialog(room, controller, this::initTableModel);
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
                controller.deleteRoom(id);
                initTableModel();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class RoomTableModel extends AbstractTableModel {
        private final List<Room> rooms;
        private final String[] columns = {"ID", "Area", "Cost", "Max Guests", "Amenities", "Type ID"};

        public RoomTableModel(List<Room> rooms) {
            this.rooms = rooms;
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