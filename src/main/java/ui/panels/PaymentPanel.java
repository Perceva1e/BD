package ui.panels;

import controller.PaymentController;
import model.Payment;
import ui.components.TablePanel;
import ui.dialogs.PaymentFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class PaymentPanel extends TablePanel {
    private final PaymentController controller = new PaymentController();

    public PaymentPanel() {
        super("Payments Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            List<Payment> payments = controller.getAllPayments();
            table.setModel(new PaymentTableModel(payments));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void onAdd() {
        new PaymentFormDialog(null, controller, this::initTableModel);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);
            try {
                Payment payment = controller.getPaymentById(id);
                new PaymentFormDialog(payment, controller, this::initTableModel);
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
                controller.deletePayment(id);
                initTableModel();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class PaymentTableModel extends AbstractTableModel {
        private final List<Payment> payments;
        private final String[] columns = {"ID", "Type", "Date", "Method", "Status", "Booking ID"};

        public PaymentTableModel(List<Payment> payments) {
            this.payments = payments;
        }

        @Override public int getRowCount() { return payments.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

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