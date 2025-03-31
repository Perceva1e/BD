package ui.panels;

import controller.RoomTypeController;
import model.RoomType;
import ui.components.TablePanel;
import ui.dialogs.RoomTypeFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class RoomTypePanel extends TablePanel {
    private final RoomTypeController controller = new RoomTypeController();

    public RoomTypePanel() {
        super("Room Types Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            List<RoomType> types = controller.getAllRoomTypes();
            table.setModel(new RoomTypeTableModel(types));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void onAdd() {
        new RoomTypeFormDialog(null, controller, this::initTableModel);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);
            try {
                RoomType type = controller.getRoomTypeById(id);
                new RoomTypeFormDialog(type, controller, this::initTableModel);
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
                controller.deleteRoomType(id);
                initTableModel();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class RoomTypeTableModel extends AbstractTableModel {
        private final List<RoomType> types;
        private final String[] columns = {"ID", "Name", "Comfort", "Category", "Cost/Night"};

        public RoomTypeTableModel(List<RoomType> types) {
            this.types = types;
        }

        @Override public int getRowCount() { return types.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            RoomType type = types.get(row);
            return switch (col) {
                case 0 -> type.getId();
                case 1 -> type.getName();
                case 2 -> type.getComfortLevel();
                case 3 -> type.getCategory();
                case 4 -> "$" + type.getCostPerNight();
                default -> null;
            };
        }
    }
}