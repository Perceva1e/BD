package ui.panels;

import controller.EmployeeController;
import model.Employee;
import ui.components.TablePanel;
import ui.dialogs.EmployeeFormDialog;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class EmployeePanel extends TablePanel {
    private final EmployeeController controller = new EmployeeController();
    private final BookingPanel bookingPanel; // Ссылка на BookingPanel

    public EmployeePanel(BookingPanel bookingPanel) {
        super("Employees Management");
        this.bookingPanel = bookingPanel;
        initTableModel();
    }

    private void initTableModel() {
        try {
            refreshData();
        } catch (SQLException ex) {
            showError(ex.getMessage());
            table.setModel(new EmployeeTableModel(Collections.emptyList()));
        }
    }

    @Override
    protected void onAdd() {
        new EmployeeFormDialog(null, controller, this::safeRefresh);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            try {
                int id = (int) table.getModel().getValueAt(row, 0);
                Employee employee = controller.getEmployeeById(id);
                new EmployeeFormDialog(employee, controller, this::safeRefresh);
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

            try {
                // Проверяем, существует ли сотрудник
                if (!controller.employeeExists(id)) {
                    showError("Employee not found!");
                    return;
                }

                // Находим другого сотрудника для переназначения бронирований
                Integer replacementId = controller.findFirstEmployeeId();
                if (replacementId != null && replacementId == id) {
                    replacementId = controller.findFirstEmployeeId(id);
                }

                if (replacementId == null) {
                    showError("Cannot delete - no other employees available!");
                    return;
                }

                // Подтверждение удаления
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete this employee? All bookings will be reassigned to employee ID: " + replacementId,
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Переназначаем бронирования и удаляем сотрудника
                    controller.deleteEmployeeWithReassignment(id, replacementId);
                    safeRefresh();
                    if (bookingPanel != null) {
                        bookingPanel.refreshData(); // Обновляем BookingPanel
                    }
                    JOptionPane.showMessageDialog(
                            this,
                            "Employee deleted successfully! Bookings reassigned to employee ID: " + replacementId,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Deletion canceled",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (SQLException ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        } else {
            showError("Please select an employee to delete");
        }
    }

    private void refreshData() throws SQLException {
        List<Employee> employees = controller.getAllEmployees();
        table.setModel(new EmployeeTableModel(employees));
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

    private static class EmployeeTableModel extends AbstractTableModel {
        private final List<Employee> employees;
        private final String[] columns = {"ID", "Name", "Position", "Salary", "Schedule"};

        public EmployeeTableModel(List<Employee> employees) {
            this.employees = employees != null ? employees : Collections.emptyList();
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
                case 3 -> "$" + employee.getSalary();
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