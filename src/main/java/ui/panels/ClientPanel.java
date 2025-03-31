package ui.panels;

import controller.ClientController;
import model.Client;
import ui.components.TablePanel;
import ui.dialogs.ClientFormDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class ClientPanel extends TablePanel {
    private final ClientController controller = new ClientController();

    public ClientPanel() {
        super("Clients Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            List<Client> clients = controller.getAllClients();
            table.setModel(new ClientTableModel(clients));
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    @Override
    protected void onAdd() {
        new ClientFormDialog(null, controller, this::refreshData);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            try {
                int id = (int) table.getModel().getValueAt(row, 0);
                Client client = controller.getClientById(id);
                if (client != null) {
                    new ClientFormDialog(client, controller, this::refreshData);
                }
            } catch (SQLException ex) {
                showError("Error loading client: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete client and all related data?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteClient(id);
                    refreshData();
                } catch (SQLException ex) {
                    showError("Error deleting client: " + ex.getMessage());
                }
            }
        }
    }

    private void refreshData() {
        initTableModel();
        table.repaint();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class ClientTableModel extends AbstractTableModel {
        private final List<Client> clients;
        private final String[] columns = {"ID", "Full Name", "Phone", "Passport", "Birth Date"};

        public ClientTableModel(List<Client> clients) {
            this.clients = clients;
        }

        @Override public int getRowCount() { return clients.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Client client = clients.get(row);
            return switch (col) {
                case 0 -> client.getId();
                case 1 -> client.getFullName();
                case 2 -> client.getPhone();
                case 3 -> client.getPassportNumber();
                case 4 -> client.getBirthDate().toString();
                default -> null;
            };
        }
    }
}