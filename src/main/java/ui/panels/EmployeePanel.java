package ui.panels;

import controller.EmployeeController;
import model.Employee;
import ui.components.TablePanel;
import ui.dialogs.EmployeeFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeePanel extends TablePanel {
    private final EmployeeController controller = new EmployeeController();

    public EmployeePanel() {
        super("Employees Management");
        initTableModel();
    }

    private void initTableModel() {
        try {
            List<Employee> employees = controller.getAllEmployees();
            table.setModel(new EmployeeTableModel(employees));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void onAdd() {
        new EmployeeFormDialog(null, controller, this::initTableModel);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getModel().getValueAt(row, 0);
            try {
                Employee employee = controller.getEmployeeById(id);
                new EmployeeFormDialog(employee, controller, this::initTableModel);
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
                controller.deleteEmployee(id);
                initTableModel();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class EmployeeTableModel extends AbstractTableModel {
        private final List<Employee> employees;
        private final String[] columns = {"ID", "Name", "Position", "Salary", "Schedule"};

        public EmployeeTableModel(List<Employee> employees) {
            this.employees = employees;
        }

        @Override public int getRowCount() { return employees.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Employee employee = employees.get(row);
            return switch (col) {
                case 0 -> employee.getId();
                case 1 -> employee.getFullName();
                case 2 -> employee.getPosition();
                case 3 -> employee.getSalary();
                case 4 -> employee.getWorkSchedule() != null ?
                        String.join(", ", employee.getWorkSchedule()
                                .stream()
                                .map(LocalDate::toString)
                                .toList()) : "No schedule";
                default -> null;
            };
        }
    }
}