package ui.panels;

import controller.PaymentController;
import model.Payment;
import ui.components.TablePanel;
import ui.dialogs.PaymentFormDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PaymentPanel extends TablePanel implements Refreshable {
    private final PaymentController controller = new PaymentController();

    public PaymentPanel() {
        super("Payments Management");
        initialize();
    }

    private void initialize() {
        try {
            refreshDataInternal();
        } catch (SQLException ex) {
            showError("Initialization error: " + ex.getMessage());
            table.setModel(new PaymentTableModel(Collections.emptyList()));
        }
    }

    @Override
    protected void onAdd() {
        new PaymentFormDialog(null, controller, this::safeRefresh);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Please select a payment to edit");
            return;
        }

        try {
            int id = (int) table.getModel().getValueAt(row, 0);
            Payment payment = controller.getPaymentById(id);
            new PaymentFormDialog(payment, controller, this::safeRefresh);
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Please select a payment to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected payment?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getModel().getValueAt(row, 0);
                controller.deletePayment(id);
                safeRefresh();
            } catch (SQLException ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        }
    }

    @Override
    public void refreshData() {
        try {
            refreshDataInternal();
        } catch (SQLException ex) {
            showError("Refresh error: " + ex.getMessage());
        }
    }

    private void refreshDataInternal() throws SQLException {
        List<Payment> payments = controller.getAllPayments();
        table.setModel(new PaymentTableModel(payments));
        table.repaint();
    }

    private void safeRefresh() {
        try {
            refreshDataInternal();
        } catch (SQLException ex) {
            showError("Refresh error: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class PaymentTableModel extends AbstractTableModel {
        private final List<Payment> payments;
        private final String[] columns = {"ID", "Type", "Date", "Method", "Status", "Booking ID"};

        public PaymentTableModel(List<Payment> payments) {
            this.payments = payments != null ? payments : Collections.emptyList();
        }

        @Override
        public int getRowCount() {
            return payments.size();
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
            Payment payment = payments.get(row);
            return switch (col) {
                case 0 -> payment.getId();
                case 1 -> payment.getPaymentType();
                case 2 -> payment.getPaymentDate().toString();
                case 3 -> payment.getPaymentMethod();
                case 4 -> payment.getStatus();
                case 5 -> payment.getBookingId();
                default -> null;
            };
        }
    }
}