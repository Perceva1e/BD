package ui.panels;

import controller.ServiceController;
import model.Service;
import ui.components.TablePanel;
import ui.dialogs.ServiceFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ServicePanel extends TablePanel {
    private final ServiceController controller = new ServiceController();

    public ServicePanel() {
        super("Services Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            refreshData();
        } catch (SQLException ex) {
            showError(ex.getMessage());
            table.setModel(new ServiceTableModel(Collections.emptyList()));
        }
    }

    @Override
    protected void onAdd() {
        new ServiceFormDialog(null, controller, this::safeRefresh);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            try {
                int id = (int) table.getModel().getValueAt(row, 0);
                Service service = controller.getServiceById(id);
                new ServiceFormDialog(service, controller, this::safeRefresh);
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
                    "Delete this service?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteService(id);
                    safeRefresh();
                } catch (SQLException ex) {
                    showError("Delete failed: " + ex.getMessage());
                }
            }
        }
    }

    private void refreshData() throws SQLException {
        List<Service> services = controller.getAllServices();
        table.setModel(new ServiceTableModel(services));
        table.repaint();
    }

    private void safeRefresh() {
        try {
            refreshData();
        } catch (SQLException ex) {
            showError("Failed to refresh: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class ServiceTableModel extends AbstractTableModel {
        private final List<Service> services;
        private final String[] columns = {"ID", "Name", "Category", "Cost", "Duration"};

        public ServiceTableModel(List<Service> services) {
            this.services = services != null ? services : Collections.emptyList();
        }

        @Override public int getRowCount() { return services.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Service service = services.get(row);
            return switch (col) {
                case 0 -> service.getId();
                case 1 -> service.getName();
                case 2 -> service.getCategory();
                case 3 -> "$" + service.getCost();
                case 4 -> service.getFormattedDuration();
                default -> null;
            };
        }
    }
}